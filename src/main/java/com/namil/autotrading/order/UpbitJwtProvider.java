package com.namil.autotrading.order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.namil.autotrading.config.UpbitProperties;
import com.namil.autotrading.dto.UpbitOrderRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UpbitJwtProvider {

    private final UpbitProperties upbitProperties;

    public UpbitJwtProvider(UpbitProperties upbitProperties) {
        this.upbitProperties = upbitProperties;
    }

    public String createOrderTestToken(Map<String, Object> params) {
        String queryString = buildQueryString(params);
        System.out.println("queryString = " + queryString);

        String queryHash = sha512(queryString);

        byte[] secretKeyBytes = upbitProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);

        Algorithm algorithm;

        try {
            algorithm = Algorithm.HMAC512(secretKeyBytes);
        } finally {
            Arrays.fill(secretKeyBytes, (byte) 0);
        }

        JWTCreator.Builder builder = JWT.create()
                .withHeader(Collections.singletonMap("alg", "HS512"))
                .withClaim("access_key", upbitProperties.getAccessKey())
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512");

        return builder.sign(algorithm);
    }

    private String buildQueryString(Map<String, Object> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for(byte b : digest) {
                sb.append(String.format("%02x",b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("SHA-512 해시 생성 실패", e);
        }
    }

    public String createTokenWithoutQuery() {
        byte[] secretKeyBytes = upbitProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);

        Algorithm algorithm;
        try {
            algorithm = Algorithm.HMAC512(secretKeyBytes);
        } finally {
            Arrays.fill(secretKeyBytes, (byte) 0);
        }

        return JWT.create()
                .withHeader(Map.of("alg", "HS512", "typ", "JWT"))
                .withClaim("access_key", upbitProperties.getAccessKey())
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);
    }
}
