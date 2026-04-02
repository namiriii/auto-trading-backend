package com.namil.autotrading.domain.strategy;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

//가격 전략 : 현재 가격이 목표 가격 이하이면 만족
@Component
public class PriceStrategy implements OrderStrategy{


    @Override
    public StrategyType getType() {
        return StrategyType.PRICE;
    }

    @Override
    public boolean isSatisfied() {
        int currentPrice = ThreadLocalRandom.current().nextInt(90000000,110000001);
        int targetPrice = 100000000;

        System.out.println("현재 가격: " + currentPrice);

        return currentPrice <= targetPrice;
    }

    @Override
    public String getName() {
        return "PRICE";
    }
}
