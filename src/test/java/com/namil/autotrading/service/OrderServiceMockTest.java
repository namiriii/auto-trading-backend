package com.namil.autotrading.service;

import com.namil.autotrading.domain.strategy.OrderStrategy;
import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.price.AveragePriceProvider;
import com.namil.autotrading.price.PriceProvider;
import com.namil.autotrading.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class OrderServiceMockTest {

    @Test
    void mock_기반으로_OrderService_생성() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderStrategy priceStrategy = mock(OrderStrategy.class);
        OrderStrategy readyCountStrategy = mock(OrderStrategy.class);
        PriceProvider priceProvider = mock(PriceProvider.class);
        AveragePriceProvider averagePriceProvider = mock(AveragePriceProvider.class);

        OrderService orderService = new OrderService(
                orderRepository,
                List.of(priceStrategy,readyCountStrategy),
                priceProvider,
                averagePriceProvider
        );
    }

    @Test
    void 모든_전략이_만족하면_주문_생성() {
        //given
        //가짜 repository 만들기
        OrderRepository orderRepository = mock(OrderRepository.class);
        //가짜 전략 만들기 가격전략
        OrderStrategy priceStrategy = mock(OrderStrategy.class);
        //가짜 전략 만들기 레디카운트전략
        OrderStrategy readyCountStrategy = mock(OrderStrategy.class);
        PriceProvider priceProvider = mock(PriceProvider.class);
        AveragePriceProvider averagePriceProvider = mock(AveragePriceProvider.class);

        //priceStrategy.getType()이 불리면 PRICE를 리턴하라고 설정
        when(priceStrategy.getType()).thenReturn(StrategyType.PRICE);
        //readyCountStrategy.getType()이 불리면 READY_COUNT를 리턴하라고 설정
        when(readyCountStrategy.getType()).thenReturn(StrategyType.READY_COUNT);

        //가짜 전략 이름 넣어주기
        when(priceStrategy.getName()).thenReturn("PRICE");
        when(readyCountStrategy.getName()).thenReturn("READY_COUNT");

        //priceStrategy.isSatisfied()에 어떤 context가 들어오던(any()) 무조건 true반환
        when(priceStrategy.isSatisfied(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        //readyCountStrategy.isSatisfied()에 어떤 context가 들어오던(any()) 무조건 true반환
        when(readyCountStrategy.isSatisfied(org.mockito.ArgumentMatchers.any())).thenReturn(true);

        when(priceProvider.getCurrentPrice()).thenReturn(100000000);
        when(averagePriceProvider.getAveragePrice(anyInt())).thenReturn(100000000.0);

        //save 호출 시 전달된 Order 객체 그대로 반환
        //save 호출되면 그때 들어온 값을 그대로 돌려달라고 설정 save(order)하면 order를 그대로 반환
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //진짜 OrderService 객체 만듦
        //안에 넣는 데이터는 가짜
        OrderService orderService = new OrderService(
                orderRepository,
                List.of(priceStrategy,readyCountStrategy),
                priceProvider,
                averagePriceProvider

        );

        //when
        orderService.createOrdersByStrategies(List.of(
                StrategyType.PRICE,
                StrategyType.READY_COUNT
        ));

        //then
        //orderRepository.save()가 진짜 1번 호출됐는지 검사
        verify(orderRepository, times(1)).save(any());

    }

    @Test
    void 전략_하나라도_실패하면_주문_생성_안함() {
        //given
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderStrategy priceStrategy = mock(OrderStrategy.class);
        OrderStrategy readyCountStrategy = mock(OrderStrategy.class);
        PriceProvider priceProvider = mock(PriceProvider.class);
        AveragePriceProvider averagePriceProvider = mock(AveragePriceProvider.class);

        when(priceStrategy.getType()).thenReturn(StrategyType.PRICE);
        when(readyCountStrategy.getType()).thenReturn(StrategyType.READY_COUNT);

        when(priceStrategy.getName()).thenReturn("PRICE");
        when(readyCountStrategy.getName()).thenReturn("READY_COUNT");

        //PRICE는 실패
        when(priceStrategy.isSatisfied(any())).thenReturn(false);

        //READY_COUNT는 성공
        when(readyCountStrategy.isSatisfied(any())).thenReturn(true);

        when(priceProvider.getCurrentPrice()).thenReturn(100000000);
        when(averagePriceProvider.getAveragePrice(anyInt())).thenReturn(100000000.0);

        OrderService orderService = new OrderService(
                orderRepository,
                List.of(priceStrategy, readyCountStrategy),
                priceProvider,
                averagePriceProvider
        );

        //when
        orderService.createOrdersByStrategies(List.of(
                StrategyType.PRICE,
                StrategyType.READY_COUNT
        ));

        //then
        verify(orderRepository, never()).save(any());
    }
}
