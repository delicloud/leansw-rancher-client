package com.thoughtworks.lean.rancher.dto;

public class ProjectInfo {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public ProjectInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectInfo setName(String name) {
        this.name = name;
        return this;
    }
}
