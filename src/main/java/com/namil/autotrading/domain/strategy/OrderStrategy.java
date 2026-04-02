package com.namil.autotrading.domain.strategy;

//주문 전략 인터페이스
public interface OrderStrategy {

    //이 전략이 어떤 타입인지 반환
    StrategyType getType();

    //주문 가능 여부 판단
    boolean isSatisfied(StrategyContext context);

    //전략 이름 반환
    String getName();
}
