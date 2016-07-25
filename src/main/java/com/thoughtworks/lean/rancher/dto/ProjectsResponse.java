package com.thoughtworks.lean.rancher.dto;

import java.util.List;

public class ProjectsResponse {
    private List<ProjectInfo> data;

    public List<ProjectInfo> getData() {
        return data;
    }

    public ProjectsResponse setData(List<ProjectInfo> data) {
        this.data = data;
        return this;
    }
}
