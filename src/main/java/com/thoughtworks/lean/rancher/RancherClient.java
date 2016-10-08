package com.thoughtworks.lean.rancher;

import com.thoughtworks.lean.rancher.dto.*;

import java.util.List;
import java.util.Map;

public interface RancherClient {
    ServiceInfo serviceInfoByName(String projectName, String serviceName);

    List<ServiceInstance> serviceInstances(String projectId, String serviceId);

    List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName);

    List<ProjectInfo> projects();

    ProjectInfo projectByName(String projectName);

    ServiceInfo serviceScaleChange(ServiceInfo serviceInfo, int scale);

    ServiceInstance setInstanceLabels(String projectId, String instanceId, Map<String, String> labelsKV);

    List<EnvironmentInfo> environmentsByProjectName(String projectName);

    EnvironmentInfo environmentInfoByName(String projectName, String environmentName);

    List<ServiceInfo> servicesByEnvironmentName(String projectName, String environmentName);

}
