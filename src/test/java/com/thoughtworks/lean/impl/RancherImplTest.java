package com.thoughtworks.lean.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.EnvironmentInfo;
import com.thoughtworks.lean.rancher.dto.ProjectInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import com.thoughtworks.lean.rancher.dto.request.InstanceStopAction;
import com.thoughtworks.lean.rancher.impl.RancherClientImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;

public class RancherImplTest {
    public static final String STR_ENV_DEFAULT = "Default";
    public static final String STR_STACK = "leansw-go-agents";
    public static final String STR_SERVICE_NAME = "go-agent16-9-0-dind";
    public static final String STR_CONTAINER_NAME = "go-agent16-9-0-dind";
    RancherClient rancherClient;

    @Before
    public void setup() {
        rancherClient = new RancherClientImpl("http://rancher.dev.twleansw.com:9080/",
                "2B9B6D13870BF74B6E65",
                "Bsk9o44zMUjAo7QawdXiK7mvRiGRTukTfDswDZSJ");

    }

    @Test
    public void should_return_projects() {

        // given
        List<ProjectInfo> projects = rancherClient.projects();

        // then
        assertTrue(projects.size() > 0);
        assertNotNull(projects.get(0).getId());
        assertNotNull(projects.get(0).getName());
    }

    @Test
    public void should_get_project_by_name() {
        ProjectInfo projectInfo = rancherClient.projectByName(STR_ENV_DEFAULT);
        assertEquals(projectInfo.getName(), STR_ENV_DEFAULT);
    }

    @Test
    public void should_get_service_by_name() {
        String serviceName = "gocd-server";
        ServiceInfo serviceInfo = rancherClient.serviceInfoByName(STR_ENV_DEFAULT, serviceName);
        assertEquals(serviceInfo.getName(), serviceName);
    }


    @Test
    @Ignore
    public void should_get_environment_by_name() {
        String environmentName = "leansw-go-agents";
        EnvironmentInfo environmentInfo = rancherClient.environmentInfoByName(STR_ENV_DEFAULT, environmentName);
        assertEquals(environmentInfo.getName(), environmentName);
    }

    @Test
    @Ignore
    public void should_get_services_by_environment_name() {
        List<ServiceInfo> servicesResponse = rancherClient.servicesByEnvironmentName(STR_ENV_DEFAULT, STR_STACK);
        assertTrue(servicesResponse.size() > 0);

    }


    @Test
    @Ignore
    public void should_get_instance() {
        ServiceInstance instance = rancherClient.instance(STR_ENV_DEFAULT, STR_SERVICE_NAME, STR_CONTAINER_NAME, 1);
        assertNotNull(instance);
        assertEquals(instance.getName(), "leansw-go-agents_go-agent16-9-0-dind_1");
    }


    @Test
    @Ignore
    public void should_stop_instance() {
        ServiceInstance instance = rancherClient.instance(STR_ENV_DEFAULT, STR_SERVICE_NAME, STR_CONTAINER_NAME, 2);
        instance = rancherClient.instanceActionById(instance.getAccountId(), instance.getId(), "stop", new InstanceStopAction());
        assertEquals(instance.getState(), "stopping");
    }

    @Test
    @Ignore
    public void should_start_instance() {
        ServiceInstance instance = rancherClient.instance(STR_ENV_DEFAULT, STR_SERVICE_NAME, STR_CONTAINER_NAME, 2);
        instance = rancherClient.instanceActionById(instance.getAccountId(), instance.getId(), "start", null);
        assertEquals(instance.getState(), "starting");
    }


    @Test
    @Ignore
    public void should_get_instance_by_external_id_prefix() {
        List<ServiceInstance> instances = rancherClient.instanceByExtPrefix(STR_ENV_DEFAULT, STR_SERVICE_NAME, "7a142dc0370c");
        assertTrue(instances.size() > 0);
    }

    @Test
    @Ignore
    public void should_get_service_instances_by_name() {
        List<ServiceInstance> response = rancherClient.serviceInstancesByName(STR_ENV_DEFAULT, STR_SERVICE_NAME);
        try {
            System.out.println(new ObjectMapper().writer().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue(response.size() > 0);

    }

    @Test
    @Ignore
    public void should_get_service_instances_no_sidekick_by_name() {
        List<ServiceInstance> response = rancherClient.serviceInstancesByName(STR_ENV_DEFAULT, STR_SERVICE_NAME, false);
        try {
            System.out.println(new ObjectMapper().writer().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue(response.size() > 0);

    }

    @Test
    @Ignore
    public void should_get_service_info_by_name() {
        ServiceInfo serviceInfo = rancherClient.serviceInfoByName(STR_ENV_DEFAULT, STR_SERVICE_NAME);
        assertNotNull(serviceInfo);
        assertEquals(STR_CONTAINER_NAME, serviceInfo.getName());
    }


    @Test
    public void should_is_side_kick() {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setName("kibana_nginx-proxy_1");
        assertFalse(serviceInstance.isSideKick());
    }

}
