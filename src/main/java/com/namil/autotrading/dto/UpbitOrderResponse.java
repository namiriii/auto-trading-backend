package com.namil.autotrading.dto;

public class UpbitOrderResponse {

    private String uuid;
    private String side;
    private String ord_type;
    private String price;
    private String state;
    private String market;
    private String created_at;
    private String executed_volume;
    private Integer trades_count;

    public String getUuid() {
        return uuid;
    }

    public String getSide() {
        return side;
    }

    public String getOrd_type() {
        return ord_type;
    }

    public String getPrice() {
        return price;
    }

    public String getState() {
        return state;
    }

    public String getMarket() {
        return market;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getExecuted_volume() {
        return executed_volume;
    }

    public Integer getTrades_count() {
        return trades_count;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setOrd_type(String ord_type) {
        this.ord_type = ord_type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setExecuted_volume(String executed_volume) {
        this.executed_volume = executed_volume;
    }

    public void setTrades_count(Integer trades_count) {
        this.trades_count = trades_count;
    }
}
