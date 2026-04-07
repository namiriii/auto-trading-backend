package com.namil.autotrading.order;

import com.namil.autotrading.config.UpbitProperties;
import com.namil.autotrading.dto.UpbitOrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpbitOrderProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UpbitProperties upbitProperties;

    public UpbitOrderProvider(UpbitProperties upbitProperties) {
        this.upbitProperties = upbitProperties;
    }

    public void createTestOrder() {
        System.out.println("accessKey = " + upbitProperties.getAccessKey());
        String url = "https://api.upbit.com/v1/orders/test";

        UpbitOrderRequest request = new UpbitOrderRequest();
        request.setMarket("KRW-BTC");
        request.setSide("bid");
        request.setPrice("10000");
        request.setOrd_type("price");

        String response = restTemplate.postForObject(
                url,request,String.class
        );

        System.out.println(response);

    }
}
