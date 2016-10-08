package com.thoughtworks.lean.rancher.dto;

public class ServiceInstance {
    private String externalId;
    private String state;
    private String name;

    public String getName() {
        return name;
    }

    public ServiceInstance setName(String name) {
        this.name = name;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public ServiceInstance setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getState() {
        return state;
    }

    public ServiceInstance setState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "externalId='" + externalId + '\'' +
                '}';
    }
}
