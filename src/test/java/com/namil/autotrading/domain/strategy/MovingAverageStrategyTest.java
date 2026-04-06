package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.config.StrategyConfigProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovingAverageStrategyTest {

    @Test
    void 현재가격이_평균가격보다_낮으면_전략_만족() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.MovingAverage movingAverage = new StrategyConfigProperties.MovingAverage();
        movingAverage.setPeriod(5);
        properties.setMovingAverage(movingAverage);

        MovingAverageStrategy strategy = new MovingAverageStrategy(properties);
        StrategyContext context = new StrategyContext(95000000,0,100000000.0);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();

    }

    @Test
    void 현재가격이_평균가격보다_높으면_전략_불만족() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.MovingAverage movingAverage = new StrategyConfigProperties.MovingAverage();
        movingAverage.setPeriod(5);
        properties.setMovingAverage(movingAverage);

        MovingAverageStrategy strategy = new MovingAverageStrategy(properties);
        StrategyContext context = new StrategyContext(105000000,0,100000000.0);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }
}
