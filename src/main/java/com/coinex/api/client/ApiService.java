package com.coinex.api.client;

import com.coinex.api.client.constant.ApiConstants;
import com.coinex.api.client.model.account.Asset;
import com.coinex.api.client.model.basic.CoinPair;
import com.coinex.api.client.model.market.Candlestick;
import com.coinex.api.client.model.market.Depth;
import com.coinex.api.client.model.market.Ticker;
import com.coinex.api.client.model.market.Trade;
import com.coinex.api.client.model.order.Order;
import com.coinex.api.client.response.ApiResp;
import retrofit2.Call;
import retrofit2.http.*;

import java.math.BigDecimal;
import java.util.List;

public interface ApiService {

    @GET("/api/v1/basic/systemclock")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<Long>> getServerClock();

    @GET("/api/v1/basic/coinpairs")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<List<CoinPair>>> getCoinPairs();

    @GET("/api/v1/market/tickers")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<List<Ticker>>> getTickers(@Query("coinPair") String coinPair);

    @GET("/api/v1/market/kline")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<List<Candlestick>>> getKline(@Query("coinPair") String coinPair,
                                              @Query("period") String period,
                                              @Query("limit") Integer limit);

    @GET("/api/v1/market/depth")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<Depth>> getDepth(@Query("coinPair") String coinPair);

    @GET("/api/v1/market/trades")
    @Headers(ApiConstants.ACCESS_KEY_HEADER)
    Call<ApiResp<List<Trade>>> getTrades(@Query("coinPair") String coinPair,
                                         @Query("direction") Integer direction,
                                         @Query("startTime") String startTime,
                                         @Query("endTime") String endTime,
                                         @Query("limit") Integer limit);

    @POST("/api/v1/order/new")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<Long>> newOrder(@Query("coinPair") String coinPair,
                                 @Query("direction") Integer direction,
                                 @Query("quantity") BigDecimal quantity,
                                 @Query("price") BigDecimal price);

    @POST("/api/v1/order/cancel")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<Void>> cancelOrder(@Query("id") Long id);

    @GET("/api/v1/order/{id}")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<Order>> getOrder(@Path("id") Long orderId);

    @GET("/api/v1/order/latest")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<List<Order>>> getLatestOrders(@Query("coinPair") String coinPair,
                                               @Query("direction") Integer direction,
                                               @Query("startTime") String startTime,
                                               @Query("endTime") String endTime,
                                               @Query("limit") Integer limit);

    @GET("/api/v1/order/history")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<List<Order>>> getHistoryOrder(@Query("coinPair") String coinPair,
                                               @Query("direction") Integer direction,
                                               @Query("startTime") String startTime,
                                               @Query("endTime") String endTime,
                                               @Query("limit") Integer limit);

    @GET("/api/v1/order/filled")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<List<Trade>>> getFilledOrder(@Query("coinPair") String coinPair,
                                              @Query("direction") Integer direction,
                                              @Query("startTime") String startTime,
                                              @Query("endTime") String endTime,
                                              @Query("limit") Integer limit);

    @GET("/api/v1/account/assets")
    @Headers({ApiConstants.ACCESS_KEY_HEADER, ApiConstants.SIGNATURE_HEADER})
    Call<ApiResp<List<Asset>>> getAssets(@Query("coin") String coin);
}
