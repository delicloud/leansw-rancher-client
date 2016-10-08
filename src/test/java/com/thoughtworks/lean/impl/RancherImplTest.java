package com.thoughtworks.lean.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.EnvironmentInfo;
import com.thoughtworks.lean.rancher.dto.ProjectInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import com.thoughtworks.lean.rancher.impl.RancherClientImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;

public class RancherImplTest {
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
        String projectName = "Default";
        ProjectInfo projectInfo = rancherClient.projectByName(projectName);
        assertEquals(projectInfo.getName(), projectName);
    }

    @Test
    public void should_get_service_by_name() {
        String projectName = "Default";
        String serviceName = "gocd-server";
        ServiceInfo serviceInfo = rancherClient.serviceInfoByName(projectName, serviceName);
        assertEquals(serviceInfo.getName(), serviceName);
    }

    @Test
    @Ignore
    public void should_get_environment_by_name() {
        String projectName = "Default";
        String environmentName = "leansw-gocd-agents";
        EnvironmentInfo environmentInfo = rancherClient.environmentInfoByName(projectName, environmentName);
        assertEquals(environmentInfo.getName(), environmentName);
    }

    @Test
    @Ignore
    public void should_get_services_by_environment_name() {
        String projectName = "Default";
        String environmentName = "go-agent16-9-0-dind";
        List<ServiceInfo> servicesResponse = rancherClient.servicesByEnvironmentName(projectName, environmentName);
        //assertTrue(servicesResponse.getData().size() > 0);

    }

    @Test
    @Ignore
    public void should_get_service_instances_by_name() {
        String projectName = "Default";
        String serviceName = "go-agent16-9-0-dind";

        List<ServiceInstance> response = rancherClient.serviceInstancesByName(projectName, serviceName);
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
        String projectName = "Default";
        String serviceName = "go-agent16-9-0-dind";
        ServiceInfo serviceInfo = rancherClient.serviceInfoByName(projectName, serviceName);
        assertNotNull(serviceInfo);
        assertEquals(serviceName, serviceInfo.getName());
    }

}
