package com.thoughtworks.lean.rancher.dto;

import java.util.List;

public class EnvironmentResponse {
    private List<EnvironmentInfo> data;

    public List<EnvironmentInfo> getData() {
        return data;
    }

    public EnvironmentResponse setData(List<EnvironmentInfo> data) {
        this.data = data;
        return this;
    }

    public EnvironmentInfo getEnvironmentByName(String name) {
        return this.data.stream()
                .filter(environmentInfo -> environmentInfo.getName().equals(name))
                .findFirst().orElse(null);
    }
}
