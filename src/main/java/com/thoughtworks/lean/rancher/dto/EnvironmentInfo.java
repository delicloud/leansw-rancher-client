package com.thoughtworks.lean.rancher.dto;

public class EnvironmentInfo {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public EnvironmentInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EnvironmentInfo setName(String name) {
        this.name = name;
        return this;
    }
}
