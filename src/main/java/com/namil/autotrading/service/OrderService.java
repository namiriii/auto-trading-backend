package com.namil.autotrading.service;

import com.namil.autotrading.domain.strategy.OrderStrategy;
import com.namil.autotrading.domain.strategy.StrategyContext;
import com.namil.autotrading.dto.OrderPageResponse;
import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.exception.NotFoundException;
import com.namil.autotrading.price.AveragePriceProvider;
import com.namil.autotrading.price.PriceProvider;
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
    private final List<OrderStrategy> orderStrategies;
    private final PriceProvider priceProvider;
    private final AveragePriceProvider averagePriceProvider;

    private boolean isHolding = false;

    public boolean isHolding() {
        return isHolding;
    }

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("주문 없음"));
    }

    public OrderService(OrderRepository orderRepository, List<OrderStrategy> orderStrategies, PriceProvider priceProvider, AveragePriceProvider averagePriceProvider) {
        this.orderRepository = orderRepository;
        this.orderStrategies = orderStrategies;
        this.priceProvider = priceProvider;
        this.averagePriceProvider = averagePriceProvider;
    }

    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order(
                request.getMarket(),
                request.getSide(),
                request.getAmount(),
                OrderStatus.READY
        );

        isHolding = true;
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
    public boolean isPriceStrategySatisfied(int currentPrice, int targetPrice) {
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


    //yml에서 선택된 전략들만 검사하고, 모두 만족할 때만 주문 생성
    public void createOrdersByStrategies(List<StrategyType> strategyTypes) {

        boolean canOrder = true;

        int currentPrice = priceProvider.getCurrentPrice();
        long readyCount = orderRepository.countByStatus(OrderStatus.READY);
        double averagePrice = averagePriceProvider.getAveragePrice(currentPrice);

        System.out.println("현재 가격 : " + currentPrice);
        System.out.printf("평균 가격 : %.0f%n", averagePrice);

        StrategyContext context = new StrategyContext(
                currentPrice, readyCount, averagePrice, isHolding);

        //Spring이 주입해준 모든 전략 객체를 하나씩 확인
        for(OrderStrategy strategy : orderStrategies) {

            //현재 전략이 yml에 설정된 전략 목록에 없으면 검사하지 않고 건너뜀
            if(!strategyTypes.contains(strategy.getType())) {
                continue;
            }

            boolean satisfied = strategy.isSatisfied(context);

            if(satisfied) {
                System.out.println(strategy.getName() + " 전략 만족");
            } else {
                System.out.println(strategy.getName() + " 전략 불만족");
                canOrder = false;
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

    public void sell() {
        if(!isHolding) {
            System.out.println("보유 중이 아니라 매도 불가");
            return;
        }

        isHolding = false;

        System.out.println("매도 완료 -> holding false");
    }
}
