package com.namil.autotrading.price;


import java.util.Optional;

//현재 가격을 제공하는 인턴페이스
public interface PriceProvider {

    Optional<Integer> getCurrentPrice();
}
