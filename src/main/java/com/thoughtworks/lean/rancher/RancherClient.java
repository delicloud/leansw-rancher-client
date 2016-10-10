package com.thoughtworks.lean.rancher;

import com.thoughtworks.lean.rancher.dto.EnvironmentInfo;
import com.thoughtworks.lean.rancher.dto.ProjectInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import com.thoughtworks.lean.rancher.dto.request.InstanceStopAction;

import java.util.List;
import java.util.Map;

/**
 * 需要注意这些API中的概念与RancherUI上的概念并不一致
 */
public interface RancherClient {
    ServiceInfo serviceInfoByName(String projectName, String serviceName);

    List<ServiceInstance> serviceInstances(String projectId, String serviceId);

    List<ServiceInstance> serviceInstances(String projectId, String serviceId, boolean withSideKick);

    List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName);

    List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName, boolean withSideKick);


    ServiceInstance instance(String projectName, String serviceName, String containerName, int index);

    ServiceInstance instanceById(String projectId, String instanceId);

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

    <B> ServiceInstance instanceActionById(String accountId, String id, String action, B body);
    <B> ServiceInstance instanceAction(ServiceInstance instance, String action, B body);
    <B> ServiceInstance instanceStop(ServiceInstance instance);
    <B> ServiceInstance instanceStop(ServiceInstance instance, InstanceStopAction instanceStopAction);
    <B> ServiceInstance instanceStart(ServiceInstance instance);


}
