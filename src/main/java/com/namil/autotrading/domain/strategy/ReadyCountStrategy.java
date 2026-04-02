package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.stereotype.Component;

//READY 주문 개수가 3개 미만이면 만족
@Component
public class ReadyCountStrategy implements OrderStrategy{

    private final OrderRepository orderRepository;

    public ReadyCountStrategy(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public StrategyType getType() {
        return StrategyType.READY_COUNT;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        long readyCount = orderRepository.countByStatus(OrderStatus.READY);
        return readyCount < 3;
    }

    @Override
    public String getName() {
        return "READY_COUNT";
    }
}
