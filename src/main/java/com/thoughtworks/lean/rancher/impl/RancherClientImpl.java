package com.thoughtworks.lean.rancher.impl;

import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@Component
public class RancherClientImpl implements RancherClient {
    private String accessSecret;
    private String secretKey;
    private String rancherUrl;

    private RestTemplate restTemplate;
    private int TIMEOUT = 5000;


    @Autowired
    public RancherClientImpl(
            @Value("${rancher.uri}") String rancherUrl, @Value("${rancher.accessSecret}") String accessSecret,
            @Value("${rancher.secretKey}") String secretKey) {
        this.accessSecret = accessSecret;
        this.secretKey = secretKey;
        this.rancherUrl = rancherUrl;
        this.restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(TIMEOUT);
        requestFactory.setReadTimeout(TIMEOUT);
        restTemplate.setRequestFactory(requestFactory);
    }


    @Override
    public ProjectsResponse projects() {
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects", rancherUrl);
        ResponseEntity<ProjectsResponse> response = this.restTemplate.exchange(requestUrl, GET, request, ProjectsResponse.class);
        return response.getBody();
    }

    @Override
    public ProjectInfo projectByName(String projectName) {
        ProjectsResponse projects = projects();
        for (ProjectInfo projectInfo : projects.getData()) {
            if (projectInfo.getName().equals(projectName)) {
                return projectInfo;
            }
        }
        return null;
    }

    @Override
    public ServicesResponse servicesByProjectName(String projectName) {
        ProjectInfo projectInfo = projectByName(projectName);
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects/%s/services", rancherUrl, projectInfo.getId());
        ResponseEntity<ServicesResponse> response = this.restTemplate.exchange(requestUrl, GET, request, ServicesResponse.class);
        return response.getBody();
    }

    @Override
    public EnvironmentResponse environmentsByProjectName(String projectName) {
        ProjectInfo projectInfo = projectByName(projectName);
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects/%s/environments", rancherUrl, projectInfo.getId());
        ResponseEntity<EnvironmentResponse> response = this.restTemplate.exchange(requestUrl, GET, request, EnvironmentResponse.class);
        return response.getBody();
    }

    @Override
    public EnvironmentInfo environmentInfoByName(String projectName, String environmentName) {
        return this.environmentsByProjectName(projectName)
                .getEnvironmentByName(environmentName);
    }

    @Override
    public ServicesResponse servicesByEnvironmentName(String projectName, String environmentName) {
        ProjectInfo projectInfo = this.projectByName(projectName);
        EnvironmentInfo environmentInfo = this.environmentInfoByName(projectName,environmentName);
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects/%s/environments/%s/services", rancherUrl, projectInfo.getId(), environmentInfo.getId());
        ResponseEntity<ServicesResponse> response = this.restTemplate.exchange(requestUrl, GET, request, ServicesResponse.class);
        return response.getBody();
    }

    @Override
    public ServiceInfo serviceInfoByName(String projectName, String serviceName) {
        ServicesResponse servicesResponse = servicesByProjectName(projectName);
        return servicesResponse.getData().stream()
                .filter(serviceInfo -> serviceInfo.getName().equals(serviceName))
                .findFirst().orElseGet(null);
    }


    @Override
    public ServiceInfo serviceScaleChange(ServiceInfo serviceInfo, int scale) {

        ServiceUpdateRequest serviceUpdateRequest = new ServiceUpdateRequest();
        serviceUpdateRequest.setName(serviceInfo.getName());
        serviceUpdateRequest.setDescription(serviceInfo.getDescription());
        serviceUpdateRequest.setMetadata(serviceInfo.getMetadata());
        serviceUpdateRequest.setSelectorContainer(serviceInfo.getSelectorContainer());
        serviceUpdateRequest.setSelectorLink(serviceInfo.getSelectorLink());
        //update Scale
        serviceUpdateRequest.setScale(scale > 1 ? scale : 1);
        //

        HttpEntity<ServiceUpdateRequest> request = new HttpEntity<>(serviceUpdateRequest, buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects/%s/services/%s", rancherUrl, serviceInfo.getAccountId(), serviceInfo.getId());
        ResponseEntity<ServiceInfo> response = this.restTemplate.exchange(requestUrl, PUT, request, ServiceInfo.class);
        return response.getBody();
    }

    @Override
    public ServiceInstance setInstanceLabels(String projectId, String instanceId, Map<String, String> labelsKV) {
        return null;
    }

    private HttpHeaders buildHttpHeaders() {
        byte[] plainCredsBytes = (this.accessSecret + ":" + this.secretKey).getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);

        return headers;
    }

    @Override
    public ServiceInstancesResponse serviceInstances(String projectId, String serviceId) {
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        final String requestUrl = String.format("%s/v1/projects/%s/services/%s/instances", rancherUrl, projectId, serviceId);
        ResponseEntity<ServiceInstancesResponse> response = this.restTemplate.exchange(requestUrl, GET, request, ServiceInstancesResponse.class);
        return response.getBody();
    }

    @Override
    public ServiceInstancesResponse serviceInstancesByName(String projectName, String serviceName) {
        ServiceInfo serviceInfo = this.serviceInfoByName(projectName, serviceName);
        return this.serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId());
    }


}
