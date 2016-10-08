package com.thoughtworks.lean.rancher;

import com.thoughtworks.lean.rancher.dto.*;

import java.util.List;
import java.util.Map;

public interface RancherClient {
    ServiceInfo serviceInfoByName(String projectName, String serviceName);

    List<ServiceInstance> serviceInstances(String projectId, String serviceId);

    List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName);

    ServiceInstance instance(String projectName, String serviceName, String containerName, int index);

    List<ProjectInfo> projects();

    ProjectInfo projectByName(String projectName);

    ServiceInfo serviceScaleChange(ServiceInfo serviceInfo, int scale);

    ServiceInstance setInstanceLabels(String projectId, String instanceId, Map<String, String> labelsKV);

    List<EnvironmentInfo> environmentsByProjectName(String projectName);

    List<EnvironmentInfo> environmentsByProjectId(String projectId);

    EnvironmentInfo environmentInfoByName(String projectName, String environmentName);

    EnvironmentInfo environmentInfoById(String projectId, String environmentId);

    List<ServiceInfo> servicesByEnvironmentName(String projectName, String environmentName);

    List<ServiceInstance> instanceByExtPrefix(String strEnvDefault, String strServiceName, String extPrefix);
}
