package com.namil.autotrading.price;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomAveragePriceProvider implements AveragePriceProvider{

    @Override
    public double getAveragePrice(int currentPrice) {
        return currentPrice + ThreadLocalRandom.current().nextInt(-500000,500001);
    }
}
