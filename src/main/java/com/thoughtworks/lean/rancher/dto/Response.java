package com.thoughtworks.lean.rancher.dto;

/**
 * Created by yongliuli on 10/8/16.
 */
public class Response<T> {
    private String type;
    private T data;

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }

    public String getType() {
        return type;
    }

    public Response setType(String type) {
        this.type = type;
        return this;
    }
}
