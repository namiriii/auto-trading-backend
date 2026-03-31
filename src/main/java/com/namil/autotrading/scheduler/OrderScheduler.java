package com.namil.autotrading.scheduler;

import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.service.OrderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        orderService.createOrderIfReadyCountLessThan3();
    }
}
