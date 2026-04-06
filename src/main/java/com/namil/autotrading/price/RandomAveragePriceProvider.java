package com.namil.autotrading.price;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomAveragePriceProvider implements AveragePriceProvider{

    @Override
    public double getAveragePrice(int currentPrice) {

        int sum = 0;

        // 현재 가격 주변의 최근 가격 5개를 만든다고 가정
        for(int i=0; i<5; i++) {
            int price = currentPrice + ThreadLocalRandom.current().nextInt(-500000,500001);
            sum += price;
        }
        return (double)sum/5;
    }
}
