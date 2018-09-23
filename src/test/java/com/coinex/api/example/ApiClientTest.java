package com.coinex.api.example;

import com.coinex.api.client.ApiClient;
import com.coinex.api.client.exception.ApiException;
import com.coinex.api.client.model.account.Asset;
import com.coinex.api.client.model.basic.CoinPair;
import com.coinex.api.client.model.market.*;
import com.coinex.api.client.model.order.Order;
import com.coinex.api.client.model.order.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApiClientTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiClientTest.class);

    private static final String accessKey = "81NOabsSzPkQtPjV8LyacXIGZ1BbxWrC2xpImKRVdSKqh6Ok3oq3E3Ix36FI2wLW";
    private static final String secretKey = "aSy4SAcNSiidpaMyGJrdbnsv5oxIkLggpXKxoHjVEATkyTQ8hAw1aKkQjCOnQ8aX";
    private static ApiClient apiClient = new ApiClient(accessKey, secretKey);

    @Test
    public void testCancelOrderNormal() {
        Long orderId = apiClient.newOrder("BCH/BTC", OrderDirection.BUY, BigDecimal.valueOf(0.0001), BigDecimal.valueOf(0.0001));
        Assert.assertTrue(orderId > 0);
        apiClient.cancelOrder(orderId);
    }

    @Test
    public void testCancelOrderCanceled() {
        apiClient.cancelOrder(815323420064329l);
    }

    @Test(expected = ApiException.class)
    public void testCancelOrderInvalidId() {
        try {
            apiClient.cancelOrder(1l);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testCancelOrderEmptyId() {
        try {
            apiClient.cancelOrder(null);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetLatestOrderNormal() {
        List<Order> orders = apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, null, 3);
        Assert.assertTrue(orders.size() >= 0);
        for (Order order : orders) {
            assertOrder(order);
        }
    }

    private void assertOrder(Order order) {
        logger.info("{}", order);
        Assert.assertTrue(order.getId() > 0);
        Assert.assertTrue(order.getCoinPair().contains("/"));
        final String[] split = order.getCoinPair().split("/");
        Assert.assertEquals(split[0], order.getCoinSymbol());
        Assert.assertEquals(split[1], order.getCurrencySymbol());
        Assert.assertTrue(order.getDealPercent().contains("%"));
        Assert.assertTrue(order.getLeftQuantity().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(order.getMoney().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(order.getPrice().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(order.getQuantity().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(order.getSuccessQuantity().compareTo(BigDecimal.ZERO) >= 0);
        assertDirection(order.getDirection());
        assertOrderStatus(order.getStatus());
    }

    private void assertOrderStatus(Integer status) {
        List<Integer> statusList = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            statusList.add(orderStatus.ordinal());
        }
        Assert.assertTrue(statusList.contains(status));
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderInvalidCoinPair() {
        try {
            apiClient.getLatestOrders("LTC/BasdfasdfTC", OrderDirection.SELL, null, null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderInvalidStartTime() {
        try {
            apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, "0000-00-00", null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderInvalidEndTime() {
        try {
            apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, "0000-00-00", 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetLatestOrderEmptyLimit() {
        List<Order> orders = apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, null, null);
        Assert.assertTrue(orders.size() >= 0);
        for (Order order : orders) {
            assertOrder(order);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderBigLimit() {
        try {
            apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, null, 101);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderNegativeLimit() {
        try {
            apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, null, -1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetLatestOrderZeroLimit() {
        try {
            apiClient.getLatestOrders("LTC/BTC", OrderDirection.SELL, null, null, 0);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetHistoryOrderNormal() {
        List<Order> orders = apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, null, 3);
        Assert.assertTrue(orders.size() >= 0);
        for (Order order : orders) {
            assertOrder(order);
        }
    }

    @Test
    public void testGetHistoryOrderEmptyDirection() {
        List<Order> orders = apiClient.getHistoryOrders("LTC/BTC", null, null, null, 3);
        Assert.assertTrue(orders.size() >= 0);
        for (Order order : orders) {
            assertOrder(order);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderInvalidCoinPair() {
        try {
            apiClient.getHistoryOrders("LTC/BasdfasdfTC", OrderDirection.SELL, null, null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderInvalidStartTime() {
        try {
            apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, "0000-00-00", null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderInvalidEndTime() {
        try {
            apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, "0000-00-00", 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetHistoryOrderEmptyLimit() {
        List<Order> orders = apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, null, null);
        Assert.assertTrue(orders.size() >= 0);
        for (Order order : orders) {
            assertOrder(order);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderBigLimit() {
        try {
            apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, null, 101);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderNegativeLimit() {
        try {
            apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, null, -1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetHistoryOrderZeroLimit() {
        try {
            apiClient.getHistoryOrders("LTC/BTC", OrderDirection.SELL, null, null, 0);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetAssets() {

        List<Asset> assets = apiClient.getAssets();
        Assert.assertTrue(assets.size() > 0);
        for (Asset asset : assets) {
            assertAsset(asset);
        }

        final Asset first = apiClient.getAsset(assets.get(1).getCoin());
        assertAsset(first);
    }

    @Test(expected = ApiException.class)
    public void testGetAssetsInvalidCoin() {
        try {
            apiClient.getAsset("ETH12341");
        } catch (ApiException e) {
            logAndThrow(e);
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAssetEmptyCoin() {
        apiClient.getAsset(null);
    }

    private void assertAsset(Asset asset) {
        logger.info("{}", asset);
        Assert.assertTrue(asset.getAvailable().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(asset.getFrozen().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    public void testGetOrderNormal() {
        Order order = apiClient.getOrder(815323420064329l);
        assertOrder(order);
    }

    @Test(expected = ApiException.class)
    public void testGetOrderInvlidId() {
        try {
            apiClient.getOrder(1l);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetFilledOrderNormal() {
        List<Trade> trades = apiClient.getFilledOrders("LTC/BTC", OrderDirection.BUY, null, null, 3);
        Assert.assertTrue(trades.size() >= 0);
        for (Trade trade : trades) {
            assertTrade(trade);
        }

        trades = apiClient.getFilledOrders("LTC/BTC", OrderDirection.SELL, null, null, 3);
        Assert.assertTrue(trades.size() >= 0);
        for (Trade trade : trades) {
            assertTrade(trade);
        }

    }

    @Test
    public void testGetFilledOrderEmptyDirection() {
        List<Trade> trades = apiClient.getFilledOrders("LTC/BTC", null, null, null, 3);
        Assert.assertTrue(trades.size() >= 0);
        for (Trade trade : trades) {
            assertTrade(trade);
        }

    }

    private void assertTrade(Trade trade) {
        logger.info("{}", trade);
        Assert.assertTrue(trade.getId() > 0);
        Assert.assertTrue(trade.getCoinPair().contains("/"));
        assertDirection(trade.getDirection());
        Assert.assertTrue(trade.getTime() > 0);
        Assert.assertTrue(trade.getPrice().compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(trade.getQuantity().compareTo(BigDecimal.ZERO) >= 0);
    }

    private void assertDirection(Integer direction) {
        Assert.assertTrue(direction == OrderDirection.BUY.ordinal()
                || direction == OrderDirection.SELL.ordinal());
    }

    @Test(expected = ApiException.class)
    public void testGetFilledOrderInvalidTime() {
        try {
            apiClient.getFilledOrders("LTC/BTC", OrderDirection.BUY, "123088012", null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetFilledOrderInvalidCoinPair() {
        try {
            apiClient.getFilledOrders("LasdfasfTC/BTC", OrderDirection.BUY, null, null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetFilledOrderEmptyLimit() {
        List<Trade> trades = apiClient.getFilledOrders("LTC/BTC", OrderDirection.BUY, null, null, null);
        Assert.assertTrue(trades.size() >= 0);
        for (Trade trade : trades) {
            assertTrade(trade);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetFilledOrderBigLimit() {
        try {
            apiClient.getFilledOrders("LTC/BTC", OrderDirection.BUY, null, null, 201);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetFilledOrderNegativeLimit() {
        try {
            apiClient.getFilledOrders("LTC/BTC", OrderDirection.BUY, null, null, -1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    private void logAndThrow(ApiException e) {
        logger.error("", e);
        Assert.assertNotNull(e.getErrorCode());
        Assert.assertTrue(e.getErrorCode() > 0 && e.getErrorCode() != 1);
        throw e;
    }

    @Test(expected = ApiException.class)
    public void testNewOrderEmptyCoinPair() {
        try {
            apiClient.newOrder(null, OrderDirection.BUY, BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderInvalidCoinPair() {
        try {
            apiClient.newOrder("asdfafd", OrderDirection.BUY, BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderEmptyDirection() {
        try {
            apiClient.newOrder("LTC/BTC", null, BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderEmptyQuantity() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.SELL, null, BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderEmptyPrice() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.SELL, BigDecimal.valueOf(0.001), null);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testNewOrderNormal() {
        Long orderId = apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(0.0001), BigDecimal.valueOf(0.0001));
        Assert.assertTrue(orderId > 0);
    }

    @Test(expected = ApiException.class)
    public void testNewOrderLackOfBalance() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(10000000), BigDecimal.valueOf(10000));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderNegativeQuantity() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(-0.1), BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderZeroQuantity() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.ZERO, BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderBigQuantity() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.valueOf(0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderNegativePrice() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(0.1), BigDecimal.valueOf(-0.001));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderZeroPrice() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(0.001), BigDecimal.ZERO);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testNewOrderBigPrice() {
        try {
            apiClient.newOrder("LTC/BTC", OrderDirection.BUY, BigDecimal.valueOf(0.001), BigDecimal.valueOf(Long.MAX_VALUE));
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }


    @Test
    public void testGetTradesNormal() {
        List<Trade> trades = apiClient.getTrades("LTC/BTC", OrderDirection.BUY, null, null, 3);
        for (Trade trade : trades) {
            logger.info("{}", trade);
        }

        trades = apiClient.getTrades("LTC/BTC", OrderDirection.SELL, null, null, 3);
        for (Trade trade : trades) {
            logger.info("{}", trade);
        }

    }

    @Test
    public void testGetTradesEmptyDirection() {
        List<Trade> trades = apiClient.getTrades("LTC/BTC", null, null, null, 3);
        for (Trade trade : trades) {
            logger.info("{}", trade);
        }

    }

    @Test(expected = ApiException.class)
    public void testGetTradesInvalidTime() {
        try {
            apiClient.getTrades("LTC/BTC", OrderDirection.BUY, "asdfasf", null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetTradesInvalidCoinPair() {
        try {
            apiClient.getTrades("LasdfasfTC/BTC", OrderDirection.BUY, null, null, 3);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetTradesEmptyLimit() {
        List<Trade> trades = apiClient.getTrades("LTC/BTC", OrderDirection.BUY, null, null, null);
        for (Trade trade : trades) {
            logger.info("{}", trade);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetTradesBigLimit() {
        try {
            apiClient.getTrades("LTC/BTC", OrderDirection.BUY, null, null, 201);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetTradesNegativeLimit() {
        try {
            apiClient.getTrades("LTC/BTC", OrderDirection.BUY, null, null, -1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetDepthNormal() {
        Depth depth = apiClient.getDepth("LTC/BTC");
        logger.info("{}", depth);
        Assert.assertEquals("LTC/BTC", depth.getCoinPair());
        Assert.assertTrue(depth.getAsks().size() >= 0);
        Assert.assertTrue(depth.getBids().size() >= 0);
    }

    @Test(expected = ApiException.class)
    public void testGetDepthInvalidCoinPair() {

        try {
            apiClient.getDepth("LTCasdf/BTC");
        } catch (ApiException e) {
            logAndThrow(e);
        }

    }

    @Test(expected = ApiException.class)
    public void testGetDepthEmptyCoinPair() {

        try {
            apiClient.getDepth(null);
        } catch (ApiException e) {
            logAndThrow(e);
        }

    }

    @Test
    public void testGetKlineNormal() {
        List<Candlestick> candlesticks = apiClient.getOneMinuteKline("LTC/BTC", 3);
        Assert.assertTrue(candlesticks.size() >= 0);
        for (Candlestick candlestick : candlesticks) {
            assertCandlestick(candlestick);
        }

        candlesticks = apiClient.getFiveMinuteKline("LTC/ETH", 3);
        Assert.assertTrue(candlesticks.size() >= 0);
        for (Candlestick candlestick : candlesticks) {
            assertCandlestick(candlestick);
        }

    }

    private void assertCandlestick(Candlestick candlestick) {
        logger.info("{}", candlestick);
        Assert.assertTrue(new BigDecimal(candlestick.getClose()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(candlestick.getHigh()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(candlestick.getLow()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(candlestick.getOpen()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(candlestick.getQuantity()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(candlestick.getTime() >= 0);

    }

    @Test(expected = ApiException.class)
    public void testGetOneMinuteKlineInvalidCoinPair() {
        try {
            apiClient.getOneMinuteKline("asdf");
        } catch (ApiException e) {
            logAndThrow(e);
        }

    }

    @Test(expected = ApiException.class)
    public void testGetKlineInvalidPeriod() {
        try {
            apiClient.getKline("LTC/BTC", "112", 1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetKlineEmptyPeriod() {
        try {
            apiClient.getKline("LTC/BTC", null, 1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetKlineEmptyLimit() {
        List<Candlestick> candlesticks = apiClient.getKline("LTC/BTC", KlinePeriodType.ONE_MINUTE, null);
        Assert.assertTrue(candlesticks.size() > 0);
        for (Candlestick candlestick : candlesticks) {
            assertCandlestick(candlestick);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetKlineBigLimit() {
        try {
            apiClient.getKline("LTC/BTC", KlinePeriodType.ONE_MINUTE, 1001);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test(expected = ApiException.class)
    public void testGetKlineNegativeLimit() {
        try {
            apiClient.getKline("LTC/BTC", KlinePeriodType.ONE_MINUTE, -1);
        } catch (ApiException e) {
            logAndThrow(e);
        }
    }

    @Test
    public void testGetAllTickersNormal() {
        List<Ticker> all = apiClient.getTickers();
        for (Ticker ticker : all) {
            assertTicker(ticker);
        }

    }

    @Test
    public void testGetOneTickerNormal() {
        Ticker ticker = apiClient.getTicker("LTC/ETH");
        assertTicker(ticker);
    }

    private void assertTicker(Ticker ticker) {
        logger.info("{}", ticker);
        Assert.assertNotNull(ticker);
        Assert.assertTrue(ticker.getCoinPair().contains("/"));
        Assert.assertTrue(new BigDecimal(ticker.getBottomPrice()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(ticker.getTopPrice()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(ticker.getGross()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(ticker.getGrossVolume()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(ticker.getNewPrice()).compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(new BigDecimal(ticker.getPriceIncrease()).compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test(expected = ApiException.class)
    public void testGetTickersInvalidCoinPair() {
        apiClient.getTicker("asldkf");
    }

    @Test
    public void testGetCoinPairs() {
        List<CoinPair> coinPairs = apiClient.getCoinPairs();
        Assert.assertTrue(coinPairs.size() > 0);
        for (CoinPair coinPair : coinPairs) {
            logger.info("{}", coinPair);
            Assert.assertTrue(coinPair.getName().contains("/"));
            Assert.assertTrue(coinPair.getPricePrecision() > 0);
            Assert.assertTrue(coinPair.getQuantityPrecision() > 0);
        }
    }

    @Test
    public void testGetSystemClock() {
        final Long systemClock = apiClient.getSystemClock();
        logger.info("{}", systemClock);
        Assert.assertTrue(systemClock > 0);
        Assert.assertTrue(systemClock < System.currentTimeMillis());
    }
}
