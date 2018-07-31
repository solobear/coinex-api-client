package com.coinex.api.example.robot;

import com.coinex.api.client.ApiClient;
import com.coinex.api.client.model.account.Asset;
import com.coinex.api.client.model.basic.CoinPair;
import com.coinex.api.client.model.market.Depth;
import com.coinex.api.client.model.market.OrderDirection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class OrderRobot {

    private static final Logger logger = LoggerFactory.getLogger(OrderRobot.class);

    private String accessKey;
    private String secretKey;
    private ApiClient apiClient;
    private List<CoinPair> coinPairs;

    public OrderRobot(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        initApiClient();
        initCoinPairs();
    }

    private void initCoinPairs() {
        coinPairs = apiClient.getCoinPairs();
    }

    private void initApiClient() {
        apiClient = new ApiClient(accessKey, secretKey);
    }

    public void buy() {
        newOrder(OrderDirection.BUY);
    }

    public void sell() {
        newOrder(OrderDirection.SELL);
    }

    private void newOrder(OrderDirection direction) {
        logger.info("开始下单>>>");
        try {
            CoinPair coinPair = randomCoinPair();
            if (coinPair == null) {
                logger.warn("没有可用交易对");
                return;
            }
            String coinPairName = coinPair.getName();
            String coinName = coinPair.coinName();
            Asset asset = apiClient.getAsset(coinName);
            if (asset.getAvailable().compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("余额不足");
                return;
            }

            BigDecimal quantity;
            BigDecimal price;

            Depth depth = apiClient.getDepth(coinPairName);
            List<Depth.DepthItem> depthItems = null;
            if (OrderDirection.BUY == direction) {
                depthItems = depth.getAsks();
            }
            if (CollectionUtils.isEmpty(depthItems)) {
                price = BigDecimal.valueOf(RandomUtils.nextDouble(0.001, 0.1));
            } else {
                Depth.DepthItem suitable;
                if (OrderDirection.BUY == direction) {
                    suitable = depthItems.get(depthItems.size() - 1);
                } else {
                    suitable = depthItems.get(0);
                }
                BigDecimal suitablePrice = suitable.getPrice();
                price = suitablePrice.subtract(BigDecimal.valueOf(suitablePrice.longValue())).divide(BigDecimal.valueOf(2));
            }

            BigDecimal maxQuantity = asset.getAvailable().divide(price, coinPair.getQuantityPrecision(), ROUND_HALF_DOWN);
            quantity = BigDecimal.valueOf(RandomUtils.nextDouble(maxQuantity.divide(BigDecimal.valueOf(1000000)).doubleValue(), maxQuantity.divide(BigDecimal.valueOf(100000)).doubleValue()));

            price = price.setScale(coinPair.getPricePrecision(), ROUND_HALF_DOWN);
            quantity = quantity.setScale(coinPair.getQuantityPrecision(), ROUND_HALF_DOWN);

            Long orderId = apiClient.newOrder(coinPairName, direction, quantity, price);
            logger.info("下单成功，订单ID:{}", orderId);
        } catch (Throwable t) {
            logger.error("new order error", t);
        }
    }

    private CoinPair randomCoinPair() {
        if (CollectionUtils.isEmpty(coinPairs)) {
            return null;
        }
        return coinPairs.get(RandomUtils.nextInt(0, coinPairs.size()));
    }


}
