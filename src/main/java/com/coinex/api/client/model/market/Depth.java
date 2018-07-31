package com.coinex.api.client.model.market;

import com.coinex.api.client.model.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 深度
 */
public class Depth extends BaseModel {
    /**
     * 交易对
     */
    private String coinPair;
    /**
     * 卖盘
     */
    private List<DepthItem> asks;
    /**
     * 买盘
     */
    private List<DepthItem> bids;

    public static class DepthItem extends BaseModel {
        /**
         * 价格
         */
        private BigDecimal price;
        /**
         * 数量
         */
        private BigDecimal quantity;

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
    }

    public String getCoinPair() {
        return coinPair;
    }

    public void setCoinPair(String coinPair) {
        this.coinPair = coinPair;
    }

    public List<DepthItem> getAsks() {
        return asks;
    }

    public void setAsks(List<DepthItem> asks) {
        this.asks = asks;
    }

    public List<DepthItem> getBids() {
        return bids;
    }

    public void setBids(List<DepthItem> bids) {
        this.bids = bids;
    }
}
