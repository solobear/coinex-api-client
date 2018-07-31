package com.coinex.api.client.model.order;

public enum OrderStatus {
    /**
     * 未成交
     */
    UNFILLED,
    /**
     * 部分成交
     */
    PARTIAL_FILLED,
    /**
     * 完成成交
     */
    FILLED,
    /**
     * 撤销
     */
    CANCELED;
}
