package com.coinex.api.client.interceptor;

import com.coinex.api.client.constant.ApiConstants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class AuthenticationInterceptor implements Interceptor {

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;

    public AuthenticationInterceptor(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder newRequestBuilder = original.newBuilder();

        TreeMap<String, String> orderParamMap = new TreeMap<>();
        for (String paramName : original.url().queryParameterNames()) {
            orderParamMap.put(paramName, original.url().queryParameter(paramName));
        }
        boolean isAccessKeyRequired = original.header(ApiConstants.ACCESS_KEY) != null;
        boolean isSignatureRequired = original.header(ApiConstants.SIGNATURE) != null;
        newRequestBuilder.removeHeader(ApiConstants.ACCESS_KEY)
                .removeHeader(ApiConstants.SIGNATURE);

        HttpUrl.Builder newUrlBuilder = original.url().newBuilder();

        if (isAccessKeyRequired) {
            orderParamMap.put(ApiConstants.ACCESS_KEY, accessKey);
        }

        if (isSignatureRequired) {
            orderParamMap.put(ApiConstants.NONCE, RandomStringUtils.randomAlphanumeric(10));
            orderParamMap.put(ApiConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            String query = joinOrderedParamMap(orderParamMap);
            if (StringUtils.isNotEmpty(query)) {
                String signature = sign(query, secretKey);
                orderParamMap.put(ApiConstants.SIGNATURE, signature);
            }
        }
        for (Map.Entry<String, String> paramEntry : orderParamMap.entrySet()) {
            newUrlBuilder.setQueryParameter(paramEntry.getKey(), paramEntry.getValue());
        }
        newRequestBuilder.url(newUrlBuilder.build());
        return chain.proceed(newRequestBuilder.build());
    }

    private String joinOrderedParamMap(TreeMap<String, String> orderParamMap) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> paramEntry : orderParamMap.entrySet()) {
            stringBuffer.append(paramEntry.getKey()).append("=").append(paramEntry.getValue()).append("&");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    private String sign(String payload, String secretKey) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey.getBytes()).hmacHex(payload);
    }
}
