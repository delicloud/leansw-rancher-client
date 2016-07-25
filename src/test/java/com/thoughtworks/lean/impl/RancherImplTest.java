package com.thoughtworks.lean.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.*;
import com.thoughtworks.lean.rancher.impl.RancherClientImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RancherImplTest {
    RancherClient rancherClient;

    @Before
    public void setup() {
        rancherClient = new RancherClientImpl("http://rancher-server:8080/",
                "14E81ABF45359F074521",
                "E7xmHz2h22iJWUpf1atDEnqsd2zzv9udHdsQkjjy");

    }

    @Test
    public void should_return_projects() {

        // given
        ProjectsResponse projects = rancherClient.projects();

        // then
        assertTrue(projects.getData().size() > 0);
        assertNotNull(projects.getData().get(0).getId());
        assertNotNull(projects.getData().get(0).getName());
    }

    @Test
    public void should_get_services_by_project_name() {
        String projectName = "Default";
        ServicesResponse services = rancherClient.servicesByProjectName(projectName);
        assertTrue(services.getData().size() > 0);
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
    public void should_get_environment_by_name() {
        String projectName = "Default";
        String environmentName = "rancher-gocd-agents";
        EnvironmentInfo environmentInfo = rancherClient.environmentInfoByName(projectName, environmentName);
        assertEquals(environmentInfo.getName(), environmentName);

    }

    @Test
    public void should_get_services_by_environment_name() {
        String projectName = "Default";
        String environmentName = "rancher-gocd-agents";
        ServicesResponse servicesResponse = rancherClient.servicesByEnvironmentName(projectName, environmentName);
        //assertTrue(servicesResponse.getData().size() > 0);

    }

    @Test
    public void should_get_service_instances_by_name() {
        String projectName = "Default";
        String serviceName = "go-agent16-2-1-java8";

        ServiceInstancesResponse response = rancherClient.serviceInstancesByName(projectName, serviceName);
        try {
            System.out.println(new ObjectMapper().writer().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue(response.getData().size() > 0);

    }

}
