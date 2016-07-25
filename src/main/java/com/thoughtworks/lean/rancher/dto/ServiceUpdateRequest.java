package com.thoughtworks.lean.rancher.dto;

import java.util.Map;

public class ServiceUpdateRequest {
    private String name;
    private int scale;
    private String selectorContainer;
    private String selectorLink;
    private Map<String, String> metadata;
    private String description;

    public String getDescription() {
        return description;
    }

    public ServiceUpdateRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public ServiceUpdateRequest setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceUpdateRequest setName(String name) {
        this.name = name;
        return this;
    }

    public int getScale() {
        return scale;
    }

    public ServiceUpdateRequest setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public String getSelectorContainer() {
        return selectorContainer;
    }

    public ServiceUpdateRequest setSelectorContainer(String selectorContainer) {
        this.selectorContainer = selectorContainer;
        return this;
    }

    public String getSelectorLink() {
        return selectorLink;
    }

    public ServiceUpdateRequest setSelectorLink(String selectorLink) {
        this.selectorLink = selectorLink;
        return this;
    }

}
