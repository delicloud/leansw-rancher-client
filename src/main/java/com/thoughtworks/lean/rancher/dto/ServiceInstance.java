package com.thoughtworks.lean.rancher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.lean.rancher.dto.constants.ContainerStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstance {
    private String id;
    private String externalId;
    private String state;
    private String name;
    private String accountId;

    public boolean isSideKick() {
        return name.split("_").length == 4;
    }

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

    public String getAccountId() {
        return accountId;
    }

    public ServiceInstance setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getId() {
        return id;
    }

    public ServiceInstance setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isRunning() {
        return this.getState().equalsIgnoreCase(ContainerStatus.RUNNING);
    }

    public boolean isStoped() {
        return this.getState().equalsIgnoreCase(ContainerStatus.STOPED);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "id='" + id + '\'' +
                ", externalId='" + externalId + '\'' +
                ", state='" + state + '\'' +
                ", name='" + name + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
