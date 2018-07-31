package com.coinex.api.client;

import com.coinex.api.client.constant.ApiConstants;
import com.coinex.api.client.exception.ApiException;
import com.coinex.api.client.interceptor.AuthenticationInterceptor;
import com.coinex.api.client.model.account.Asset;
import com.coinex.api.client.model.basic.CoinPair;
import com.coinex.api.client.model.market.*;
import com.coinex.api.client.model.order.Order;
import com.coinex.api.client.response.ApiResp;
import okhttp3.OkHttpClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ApiClient {

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;

    private ApiService apiService;

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(ApiConstants.API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        initApiService();
    }

    private void initApiService() {
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(accessKey, secretKey);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 查询当前系统时间戳
     * @return
     */
    public Long getSystemClock() {
        return invoke(apiService.getServerClock());
    }

    /**
     * 获取交易对列表
     * @return
     */
    public List<CoinPair> getCoinPairs() {
        return invoke(apiService.getCoinPairs());
    }

    public List<Ticker> getTickers() {
        return invoke(apiService.getTickers(null));
    }

    public Ticker getTicker(String coinPair) {
        List<Ticker> tickers = invoke(apiService.getTickers(coinPair));
        return CollectionUtils.isEmpty(tickers) ? null : tickers.get(0);
    }

    public List<Candlestick> getOneMinuteKline(String coinPair) {
        return getOneMinuteKline(coinPair, null);
    }

    public List<Candlestick> getOneMinuteKline(String coinPair, Integer limit) {
        return getKline(coinPair, KlinePeriodType.ONE_MINUTE, limit);
    }


    public List<Candlestick> getFiveMinuteKline(String coinPair) {
        return getFiveMinuteKline(coinPair, null);
    }

    public List<Candlestick> getFiveMinuteKline(String coinPair, Integer limit) {
        return getKline(coinPair, KlinePeriodType.FIVE_MINUTE, limit);
    }

    public List<Candlestick> getKline(String coinPair, String period, Integer limit) {
        return invoke(apiService.getKline(coinPair, period, limit));
    }

    public Depth getDepth(String coinPair) {
        return invoke(apiService.getDepth(coinPair));
    }

    public List<Trade> getBuyTrades(String coinPair) {
        return getBuyTrades(coinPair, null);
    }

    public List<Trade> getBuyTrades(String coinPair, Integer limit) {
        return getBuyTrades(coinPair, null, null, limit);
    }

    public List<Trade> getBuyTrades(String coinPair, String startTime, String endTime) {
        return getBuyTrades(coinPair, startTime, endTime, null);
    }

    public List<Trade> getBuyTrades(String coinPair, String startTime, String endTime, Integer limit) {
        return getTrades(coinPair, OrderDirection.BUY, startTime, endTime, limit);
    }

    public List<Trade> getSellTrades(String coinPair) {
        return getSellTrades(coinPair, null);
    }

    public List<Trade> getSellTrades(String coinPair, Integer limit) {
        return getSellTrades(coinPair, null, null, limit);
    }

    public List<Trade> getSellTrades(String coinPair, String startTime, String endTime) {
        return getSellTrades(coinPair, startTime, endTime, null);
    }

    public List<Trade> getSellTrades(String coinPair, String startTime, String endTime, Integer limit) {
        return getTrades(coinPair, OrderDirection.BUY, startTime, endTime, limit);
    }

    public List<Trade> getTrades(String coinPair, OrderDirection direction, String startTime, String endTime, Integer limit) {
        return invoke(apiService.getTrades(coinPair, direction == null ? null : direction.ordinal(), startTime, endTime, limit));
    }

    public Order getOrder(Long orderId) {
        return invoke(apiService.getOrder(orderId));
    }

    public List<Order> getLatestOrders(String coinPair, OrderDirection direction, String startTime, String endTime, Integer limit) {
        return invoke(apiService.getLatestOrders(coinPair, direction == null ? null : direction.ordinal(), startTime, endTime, limit));
    }

    public List<Order> getHistoryOrders(String coinPair, OrderDirection direction, String startTime, String endTime, Integer limit) {
        return invoke(apiService.getLatestOrders(coinPair, direction == null ? null : direction.ordinal(), startTime, endTime, limit));
    }

    public List<Trade> getFilledBuyOrders(String coinPair, Integer limit) {
        return getFilledBuyOrders(coinPair, null, null, limit);
    }

    public List<Trade> getFilledBuyOrders(String coinPair, String startTime, String endTime) {
        return getFilledBuyOrders(coinPair, startTime, endTime, null);
    }

    public List<Trade> getFilledBuyOrders(String coinPair, String startTime, String endTime, Integer limit) {
        return getFilledOrders(coinPair, OrderDirection.BUY, startTime, endTime, limit);
    }

    public List<Trade> getFilledSellOrders(String coinPair, Integer limit) {
        return getFilledSellOrders(coinPair, null, null, limit);
    }

    public List<Trade> getFilledSellOrders(String coinPair, String startTime, String endTime) {
        return getFilledSellOrders(coinPair, startTime, endTime, null);
    }

    public List<Trade> getFilledSellOrders(String coinPair, String startTime, String endTime, Integer limit) {
        return getFilledOrders(coinPair, OrderDirection.SELL, startTime, endTime, limit);
    }


    public List<Trade> getFilledOrders(String coinPair, OrderDirection direction, String startTime, String endTime, Integer limit) {
        return invoke(apiService.getFilledOrder(coinPair, direction == null ? null : direction.ordinal(), startTime, endTime, limit));
    }

    public void cancelOrder(Long orderId) {
        invoke(apiService.cancelOrder(orderId));
    }

    public Long newOrder(String coinPair, OrderDirection direction, BigDecimal quantity, BigDecimal price) {
        return invoke(apiService.newOrder(coinPair, direction == null ? null : direction.ordinal(), quantity, price));
    }

    public Asset getAsset(String coin) {
        if (StringUtils.isEmpty(coin)) {
            throw new IllegalArgumentException();
        }
        List<Asset> assets = invoke(apiService.getAssets(coin));
        return CollectionUtils.isEmpty(assets) ? null : assets.get(0);
    }

    public List<Asset> getAssets() {
        return invoke(apiService.getAssets(null));
    }

    private <T> T invoke(Call<ApiResp<T>> call) {
        try {
            Response<ApiResp<T>> response = call.execute();
            if (response.isSuccessful()) {
                ApiResp<T> apiResp = response.body();
                if (apiResp.isOk()) {
                    return apiResp.getData();
                } else {
                    throw new ApiException(apiResp.getStatus(), apiResp.getMsg());
                }
            } else {
                throw new ApiException(response.code(), response.message());
            }
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }


}
