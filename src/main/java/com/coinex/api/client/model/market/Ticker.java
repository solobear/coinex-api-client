package com.coinex.api.client.model.market;

import com.coinex.api.client.model.BaseModel;

public class Ticker extends BaseModel {
    /**
     * 交易对
     */
    private String coinPair;
    /**
     * 最新价格
     */
    private String newPrice;
    /**
     * 24小时涨幅
     */
    private String priceIncrease;
    /**
     * 24小时最高价格
     */
    private String topPrice;
    /**
     * 24小时最低价格
     */
    private String bottomPrice;
    /**
     * 24小时交易总额
     */
    private String gross;
    /**
     * 24小时交易总量
     */
    private String grossVolume;

    public String getCoinPair() {
        return coinPair;
    }

    public void setCoinPair(String coinPair) {
        this.coinPair = coinPair;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getPriceIncrease() {
        return priceIncrease;
    }

    public void setPriceIncrease(String priceIncrease) {
        this.priceIncrease = priceIncrease;
    }

    public String getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(String topPrice) {
        this.topPrice = topPrice;
    }

    public String getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(String bottomPrice) {
        this.bottomPrice = bottomPrice;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getGrossVolume() {
        return grossVolume;
    }

    public void setGrossVolume(String grossVolume) {
        this.grossVolume = grossVolume;
    }
}
