package com.thoughtworks.lean.rancher;

import com.thoughtworks.lean.rancher.dto.*;

import java.util.Map;

public interface RancherClient {
    ServiceInfo serviceInfoByName(String projectName, String serviceName);

    ServiceInstancesResponse serviceInstances(String projectId, String serviceId);

    ServiceInstancesResponse serviceInstancesByName(String projectName, String serviceName);

    ProjectsResponse projects();

    ProjectInfo projectByName(String projectName);

    ServiceInfo serviceScaleChange(ServiceInfo serviceInfo, int scale);

    ServiceInstance setInstanceLabels(String projectId, String instanceId, Map<String, String> labelsKV);

    EnvironmentResponse environmentsByProjectName(String projectName);

    EnvironmentInfo environmentInfoByName(String projectName, String environmentName);

    ServicesResponse servicesByEnvironmentName(String projectName, String environmentName);

}
