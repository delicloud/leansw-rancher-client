package com.thoughtworks.lean.rancher.dto;

import java.util.List;

public class ServiceInstancesResponse {
    private String type;
    private List<ServiceInstance> data;

    public List<ServiceInstance> getData() {
        return data;
    }

    public ServiceInstancesResponse setData(List<ServiceInstance> data) {
        this.data = data;
        return this;
    }

    public String getType() {
        return type;
    }

    public ServiceInstancesResponse setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceInstancesResponse{" +
                "data=" + data +
                ", type='" + type + '\'' +
                '}';
    }
}
