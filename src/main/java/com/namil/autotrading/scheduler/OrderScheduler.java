package com.namil.autotrading.scheduler;

import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        int currentPrice = ThreadLocalRandom.current().nextInt(90000000,110000001);
        int targetPrice = 100000000;

        System.out.println("현재 가격 : " + currentPrice);

        orderService.createOrderByStrategy(StrategyType.PRICE,currentPrice);
    }
}
