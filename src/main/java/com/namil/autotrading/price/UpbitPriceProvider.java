package com.namil.autotrading.price;

import com.namil.autotrading.config.UpbitProperties;
import com.namil.autotrading.dto.UpbitTickerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class UpbitPriceProvider implements PriceProvider{

    private final RestTemplate restTemplate = new RestTemplate();
    private final UpbitProperties upbitProperties;

    private static final Logger log = LoggerFactory.getLogger(UpbitPriceProvider.class);

    public UpbitPriceProvider(UpbitProperties upbitProperties) {
        this.upbitProperties = upbitProperties;
    }

    @Override
    public Optional<Integer> getCurrentPrice() {

        String url = "https://api.upbit.com/v1/ticker?markets=" + upbitProperties.getMarket();

        try {
            UpbitTickerResponse[] response =
                    restTemplate.getForObject(url,UpbitTickerResponse[].class);

            if(response == null || response.length == 0) {
                throw new IllegalStateException("업비트 응답이 비어있습니다.");
            }
            return Optional.of((int)response[0].getTrade_price());

        } catch (Exception e) {
            log.error("업비트 API 호출 실패 - market: {}", upbitProperties.getMarket(),e);
            return Optional.empty();
        }

    }
}
