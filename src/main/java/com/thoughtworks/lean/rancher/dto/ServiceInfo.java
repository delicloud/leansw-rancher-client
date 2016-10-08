package com.thoughtworks.lean.rancher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInfo {
    private String id;
    private String accountId;
    private String environmentId;
    private String name;
    private String state;
    private int scale;
    private String selectorContainer;
    private String selectorLink;
    private Map<String, String> metadata;
    private String description;
    private String healthState;

    public String getId() {
        return id;
    }

    public ServiceInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public ServiceInfo setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceInfo setName(String name) {
        this.name = name;
        return this;
    }

    public int getScale() {
        return scale;
    }

    public ServiceInfo setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public String getSelectorContainer() {
        return selectorContainer;
    }

    public ServiceInfo setSelectorContainer(String selectorContainer) {
        this.selectorContainer = selectorContainer;
        return this;
    }

    public String getSelectorLink() {
        return selectorLink;
    }

    public ServiceInfo setSelectorLink(String selectorLink) {
        this.selectorLink = selectorLink;
        return this;
    }

    public String getState() {
        return state;
    }

    public ServiceInfo setState(String state) {
        this.state = state;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public ServiceInfo setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public ServiceInfo setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
        return this;
    }

    public String getHealthState() {
        return healthState;
    }

    public ServiceInfo setHealthState(String healthState) {
        this.healthState = healthState;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", environmentId='" + environmentId + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", scale=" + scale +
                ", selectorContainer='" + selectorContainer + '\'' +
                ", selectorLink='" + selectorLink + '\'' +
                ", metadata=" + metadata +
                ", description='" + description + '\'' +
                ", healthState='" + healthState + '\'' +
                '}';
    }
}
