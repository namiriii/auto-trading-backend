package com.namil.autotrading.price;

import java.util.concurrent.ThreadLocalRandom;

//랜덤 가격을 제공하는 구현체
public class RandomPriceProvider implements  PriceProvider{

    @Override
    public int getCurrentPrice() {
        return ThreadLocalRandom.current().nextInt(90000000,110000001);
    }
}
