package com.thoughtworks.lean.rancher.dto;

import java.util.List;

public class ServicesResponse {
    private List<ServiceInfo> data;

    public List<ServiceInfo> getData() {
        return data;
    }

    public ServicesResponse setData(List<ServiceInfo> data) {
        this.data = data;
        return this;
    }
}
