package com.coinex.api.client.response;

/**
 * 接口响应对象
 *
 * @param <T> 响应数据
 */
public class ApiResp<T> {
    /**
     * 状态码
     */
    private int status;
    /**
     * 消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return 1 == status;
    }
}
