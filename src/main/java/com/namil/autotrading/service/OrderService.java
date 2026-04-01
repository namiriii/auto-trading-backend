package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderPageResponse;
import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.exception.NotFoundException;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("주문 없음"));
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order(
                request.getMarket(),
                request.getSide(),
                request.getAmount(),
                OrderStatus.READY
        );

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);

    }

    //주문 목록 조회(페이지 + 정렬 + 상태 필터)
    public OrderPageResponse getOrders(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<Order> orders;

        if(status == null) {
            //전체 주문 주회
            orders = orderRepository.findAll(pageable);
        } else {
            //상태별 주문 조회
            orders = orderRepository.findByStatus(status, pageable);
        }

        return new OrderPageResponse(
                orders.getContent().stream()
                        .map(OrderResponse::from)
                        .toList(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );

    }

    public OrderResponse getOrder(Long id) {
        Order order = findOrderOrThrow(id);

        return OrderResponse.from(order);

    }

    public OrderResponse markAsOrdered(Long id) {
        Order order = findOrderOrThrow(id);

        order.executeOrder();

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);

    }

    public OrderResponse cancelOrder(Long id) {

        Order order = findOrderOrThrow(id);
//        Order order = orderRepository.findById(id)
//                .orElseThrow(()-> new NotFoundException("주문 없음"));

        order.cancelOrder();

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);

    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "createdAt"));

        return orderRepository.findByStatus(status, pageable)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    //자동 주문 생성 조건 확인, 조건 만족시 주문 생성
    public void createOrderIfConditionMet() {

        int second = LocalDateTime.now().getSecond();

        //짝수 초일때만 주문생성
        if(second % 2 == 0) {
            OrderRequest request = new OrderRequest();
            request.setMarket("KRW-BTC");
            request.setSide(OrderSide.BUY);
            request.setAmount(new BigDecimal("10000"));

            createOrder(request);

            System.out.println("자동 주문 생성됨(짝수초)");
        } else {
            System.out.println("조건 불만족 -> 주문 안함");
        }
    }

    //READY 주문 개수가 3개 이하일 때만 자동 주문 생성
    public void createOrderIfReadyCountLessThan3() {
        long readyCount = orderRepository.countByStatus(OrderStatus.READY);

        if(readyCount < 3) {
            OrderRequest request = new OrderRequest();
            request.setMarket("KRW-BTC");
            request.setSide(OrderSide.BUY);
            request.setAmount(new BigDecimal("10000"));
            
            createOrder(request);

            System.out.println("조건 만족 -> 주문 생성");
        } else {
            System.out.println("조건 불만족 -> 주문 안함");
        }
    }

    //현재 가격이 목표 가격 이하이면 자동 주문 생성
    public void createOrderIfPriceIsBelow(int currentPrice, int targetPrice) {

        if(currentPrice <= targetPrice) {
            OrderRequest request = new OrderRequest();
            request.setSide(OrderSide.BUY);
            request.setAmount(new BigDecimal("10000"));

            createOrder(request);

            System.out.println("가격 조건 만족 -> 주문 생성");
        } else {
            System.out.println("가격 조건 불만족 -> 주문 안함");
        }

    }

    //전략 기반 자동 주문
    public void createOrderByStrategy(StrategyType strategyType, int currentPrice) {

        if(strategyType == StrategyType.PRICE) {
            int targetPrice = 100000000;

            if(currentPrice <= targetPrice) {
                OrderRequest request = new OrderRequest();
                request.setMarket("KRW-BTC");
                request.setSide(OrderSide.BUY);
                request.setAmount(new BigDecimal("10000"));

                createOrder(request);

                System.out.println("PRICE 전략 -> 주문 생성");
            } else {
                System.out.println("PRICE 전략 -> 조건 불만족");
            }
        }
    }

    //가격 전략 : 현재 가격이 목표 가격 이하인지 판단
    public boolean isPriceStrategyStatisfied(int currentPrice, int targetPrice) {
        return currentPrice <= targetPrice;
    }

    //READY_COUNT 전략 : READY 주문 개수가 3개 미만인지 판단
    public boolean isReadyCountStrategySatisfied() {
        long readyCount = orderRepository.countByStatus(OrderStatus.READY);
        return readyCount < 3;
    }

    //선택한 전략에 따라 자동 주문 실행
    public void createOrderByStrategy(StrategyType strategyType) {

        if(strategyType == StrategyType.PRICE) {
            int currentPrice = ThreadLocalRandom.current().nextInt(90000000,110000001);
            int targetPrice = 100000000;

            System.out.println("현재 가격 " + currentPrice);

            createOrderIfPriceIsBelow(currentPrice,targetPrice);
        }

        if(strategyType == StrategyType.READY_COUNT) {
            createOrderIfReadyCountLessThan3();
        }

    }

    //여러 전략을 동시에 실행
    public void createOrdersByStrategies(List<StrategyType> strategies) {

        boolean canOrder = true;

        int currentPrice = ThreadLocalRandom.current().nextInt(90000000,110000001);
        int targetPrice = 100000000;

        for (StrategyType strategy : strategies) {
            if(strategy == StrategyType.PRICE) {
                if(!isPriceStrategyStatisfied(currentPrice, targetPrice)) {
                    canOrder = false;
                }
            } else if (strategy == StrategyType.READY_COUNT) {
                if(!isReadyCountStrategySatisfied()) {
                    canOrder = false;
                }
            }
        }

        if(canOrder) {
            OrderRequest request = new OrderRequest();
            request.setMarket("KRW-BTC");
            request.setSide(OrderSide.BUY);
            request.setAmount(new BigDecimal("10000"));

            createOrder(request);

            System.out.println("모든 전략 만족 -> 주문 생성");
        } else {
            System.out.println("전략 조건 불만족 -> 주문 안함");
        }
    }
}
