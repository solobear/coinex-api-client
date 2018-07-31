package com.coinex.api.client.model.basic;

import com.coinex.api.client.model.BaseModel;

public class CoinPair extends BaseModel {
    /**
     * 交易对名称
     */
    private String name;
    /**
     * 价格精度
     */
    private Integer pricePrecision;
    /**
     * 数量精度
     */
    private Integer quantityPrecision;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getQuantityPrecision() {
        return quantityPrecision;
    }

    public void setQuantityPrecision(Integer quantityPrecision) {
        this.quantityPrecision = quantityPrecision;
    }

    public String coinName() {
        return name.split("/")[0];
    }

}
