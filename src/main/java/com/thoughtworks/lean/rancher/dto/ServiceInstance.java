package com.thoughtworks.lean.rancher.dto;

public class ServiceInstance {
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public ServiceInstance setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "externalId='" + externalId + '\'' +
                '}';
    }
}
