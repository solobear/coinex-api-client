package com.coinex.api.client.model.order;

import com.coinex.api.client.model.BaseModel;

import java.math.BigDecimal;

public class Order extends BaseModel {
    private Long id;
    private String coinPair;
    private String coinSymbol;
    private String currencySymbol;
    private Integer direction;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal money;
    private BigDecimal successQuantity;
    private String dealPercent;
    private BigDecimal leftQuantity;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoinPair() {
        return coinPair;
    }

    public void setCoinPair(String coinPair) {
        this.coinPair = coinPair;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getSuccessQuantity() {
        return successQuantity;
    }

    public void setSuccessQuantity(BigDecimal successQuantity) {
        this.successQuantity = successQuantity;
    }

    public String getDealPercent() {
        return dealPercent;
    }

    public void setDealPercent(String dealPercent) {
        this.dealPercent = dealPercent;
    }

    public BigDecimal getLeftQuantity() {
        return leftQuantity;
    }

    public void setLeftQuantity(BigDecimal leftQuantity) {
        this.leftQuantity = leftQuantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
