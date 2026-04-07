package com.namil.autotrading.scheduler;

import com.namil.autotrading.config.StrategyProperties;
import com.namil.autotrading.domain.strategy.StrategyType;
import com.namil.autotrading.dto.UpbitOrderRequest;
import com.namil.autotrading.order.UpbitOrderProvider;
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
    private final StrategyProperties strategyProperties;
    private final UpbitOrderProvider orderProvider;

    public OrderScheduler(OrderService orderService, StrategyProperties strategyProperties, UpbitOrderProvider orderProvider) {
        this.orderService = orderService;
        this.strategyProperties = strategyProperties;
        this.orderProvider = orderProvider;
    }

    //5초마다 체크
    @Scheduled(fixedRate = 5000)
    public void createOrderAutomatically() {

        orderService.createOrdersByStrategies(strategyProperties.getStrategies());
    }

    @Scheduled(fixedRate = 10000)
    public void autoSell() {
        orderService.sell();
    }

    @Scheduled(fixedRate = 10000)
    public void testOrder() {
        orderProvider.createTestOrder();
        //orderProvider.testAuthOnly();
    }

}
