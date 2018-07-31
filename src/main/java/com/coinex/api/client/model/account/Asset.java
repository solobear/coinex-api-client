package com.coinex.api.client.model.account;

import com.coinex.api.client.model.BaseModel;

import java.math.BigDecimal;

public class Asset extends BaseModel {
    private String coin;
    private BigDecimal available;
    private BigDecimal frozen;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }
}
