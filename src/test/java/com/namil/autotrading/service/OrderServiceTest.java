package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderPageResponse;
import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문생성() {
        //given
        OrderRequest request = new OrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide(OrderSide.BUY);
        request.setAmount(new BigDecimal("10000"));

        //when
        OrderResponse response = orderService.createOrder(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OrderStatus.READY);

    }

    @Test
    void 주문_실행_READY에서_ORDERED로() {
        //given
        OrderRequest request = new OrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide(OrderSide.BUY);
        request.setAmount(new BigDecimal("10000"));

        OrderResponse saved = orderService.createOrder(request);

        //when
        OrderResponse result = orderService.markAsOrdered(saved.getId());

        //then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void 주문_취소_READY에서_CANCELED로() {
        //given
        OrderRequest request = new OrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide(OrderSide.BUY);
        request.setAmount(new BigDecimal("10000"));

        OrderResponse saved = orderService.createOrder(request);

        //when
        OrderResponse result = orderService.cancelOrder(saved.getId());

        //then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void 주문_취소_실패_ORDERED상태() {
        //given
        OrderRequest request = new OrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide(OrderSide.BUY);
        request.setAmount(new BigDecimal("10000"));

        OrderResponse saved = orderService.createOrder(request);
        orderService.markAsOrdered(saved.getId());

        //when & then
        assertThatThrownBy(() -> orderService.cancelOrder(saved.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("READY 상태의 주문만 취소할 수 있습니다.");
    }

    @Test
    void 주문_전체조회() {
        //given
        OrderRequest request1 = new OrderRequest();
        request1.setMarket("KRW-BTC");
        request1.setSide(OrderSide.BUY);
        request1.setAmount(new BigDecimal("10000"));

        OrderRequest request2 = new OrderRequest();
        request2.setMarket("KRW-ETH");
        request2.setSide(OrderSide.BUY);
        request2.setAmount(new BigDecimal("20000"));

        orderService.createOrder(request1);
        orderService.createOrder(request2);

        //when
        OrderPageResponse result = orderService.getOrders(null, 0, 10);

        //then
        assertThat(result.getData()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    void 주문_단건조회() {
        //given
        OrderRequest request = new OrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide(OrderSide.BUY);
        request.setAmount(new BigDecimal("10000"));

        OrderResponse saved = orderService.createOrder(request);

        //when
        OrderResponse result = orderService.getOrder(saved.getId());

        //then
        assertThat(result.getId()).isEqualTo(saved.getId());
    }

    @Test
    void 주문_단건조회_실패_없는ID() {
        //when & then
        assertThatThrownBy(()->orderService.getOrder(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 없음");
    }

    @Test
    void 주문_상태조회_READY() {
        //given
        OrderRequest request1 = new OrderRequest();
        request1.setMarket("KRW-BTC");
        request1.setSide(OrderSide.BUY);
        request1.setAmount(new BigDecimal("10000"));

        OrderRequest request2 = new OrderRequest();
        request2.setMarket("KRW-ETH");
        request2.setSide(OrderSide.BUY);
        request2.setAmount(new BigDecimal("20000"));

        OrderResponse saved1 = orderService.createOrder(request1);
        orderService.createOrder(request2);

        orderService.markAsOrdered(saved1.getId());

        //when
        OrderPageResponse result = orderService.getOrders(OrderStatus.READY,0,10);

        //then
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getStatus()).isEqualTo(OrderStatus.READY);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void 주문_페이징조회() {
        //given
        for(int i = 0; i<5; i++) {
            OrderRequest request = new OrderRequest();
            request.setMarket("KRW-BTC");
            request.setSide(OrderSide.BUY);
            request.setAmount(new BigDecimal("10000"));

            orderService.createOrder(request);
        }

        //when
        OrderPageResponse result = orderService.getOrders(null,0,2);

        //then
        assertThat(result.getData()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(2);
    }
}
