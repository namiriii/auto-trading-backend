package com.namil.autotrading.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpbitOrderRequest {

    private String market; //KRW-BTC
    private String side; //bid(매수) / ask(매도)
    private String price; //금액
    private String ord_type; //price or

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrd_type() {
        return ord_type;
    }

    public void setOrd_type(String ord_type) {
        this.ord_type = ord_type;
    }

    ;
}
