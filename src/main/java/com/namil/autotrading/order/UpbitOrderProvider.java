package com.namil.autotrading.order;

import com.namil.autotrading.config.UpbitProperties;
import com.namil.autotrading.dto.UpbitOrderRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class UpbitOrderProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UpbitProperties upbitProperties;
    private final UpbitJwtProvider upbitJwtProvider;

    public UpbitOrderProvider(UpbitProperties upbitProperties, UpbitJwtProvider upbitJwtProvider) {
        this.upbitProperties = upbitProperties;
        this.upbitJwtProvider = upbitJwtProvider;
    }

    public void createTestOrder() {

        String url = "https://api.upbit.com/v1/orders/test";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("market", "KRW-BTC");
        params.put("side", "bid");
        params.put("price", "10000");
        params.put("ord_type", "price");

        String jwtToken = upbitJwtProvider.createOrderTestToken(params);

//        UpbitOrderRequest request = new UpbitOrderRequest();
//        request.setMarket("KRW-BTC");
//        request.setSide("bid");
//        request.setPrice("10000");
//        request.setOrd_type("price");
//
//        String jwtToken = upbitJwtProvider.createOrderTestToken(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(jwtToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);


        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        System.out.println(response.getBody());

    }

    public void testAuthOnly() {
        String url = "https://api.upbit.com/v1/accounts";

        String jwtToken = upbitJwtProvider.createTokenWithoutQuery();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println(response.getBody());
    }
}
