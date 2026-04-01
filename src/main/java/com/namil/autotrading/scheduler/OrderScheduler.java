package com.namil.autotrading.scheduler;

import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
//@ConditionalOnProperty(
//        prefix = "app.scheduler",
//        name = "enabled",
//        havingValue = "true",
//        matchIfMissing = true
//)
public class OrderScheduler {

    private final OrderService orderService;

    public OrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    //5초마다 체크
    @Scheduled(fixedRate = 5000)
    public void createOrderAutomatically() {

        List<StrategyType> strategies = List.of(StrategyType.PRICE,StrategyType.READY_COUNT);

        orderService.createOrdersByStrategies(strategies);
    }
}
