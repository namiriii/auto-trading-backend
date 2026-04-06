package com.namil.autotrading.price;

import com.namil.autotrading.config.UpbitProperties;
import com.namil.autotrading.dto.UpbitTickerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpbitPriceProvider implements PriceProvider{

    private final RestTemplate restTemplate = new RestTemplate();
    private final UpbitProperties upbitProperties;

    public UpbitPriceProvider(UpbitProperties upbitProperties) {
        this.upbitProperties = upbitProperties;
    }

    @Override
    public int getCurrentPrice() {

        String url = "https://api.upbit.com/v1/ticker?markets=" + upbitProperties.getMarket();

        UpbitTickerResponse[] response =
                restTemplate.getForObject(url,UpbitTickerResponse[].class);

        return (int)response[0].getTrade_price();
    }
}
