package com.thoughtworks.lean.rancher.impl;

import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@Component
public class RancherClientImpl implements RancherClient {
    private final static String URL_PROJECT_ENVIRONMENTS = "%s/v1/projects/%s/environments";
    private final static String URL_PROJECT_ENVIRONMENT_SERVICES = "%s/v1/projects/%s/environments/%s/services";
    private final static String URL_PROJECT_SERVICES_BY_NAME = "%s/v1/projects/%s/services?name=%s";
    private final static String URL_PROJECT_SERVICE = "%s/v1/projects/%s/services/%s";
    private final static String URL_PROJECT_SERVICE_INSTANCES = "%s/v1/projects/%s/services/%s/instances";
    private final static String URL_PROJECT_SERVICE_INSTANCE = "%s/v1/projects/%s/instances/%s";
    private final static String URL_PROJECTS = "%s/v1/projects";

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
    public List<ProjectInfo> projects() {
        return get(String.format(URL_PROJECTS, rancherUrl));
    }

    @Override
    public ProjectInfo projectByName(String projectName) {
        for (ProjectInfo projectInfo : projects()) {
            if (projectInfo.getName().equals(projectName)) {
                return projectInfo;
            }
        }
        return null;
    }

    @Override
    public List<EnvironmentInfo> environmentsByProjectName(String projectName) {
        ProjectInfo projectInfo = projectByName(projectName);
        return get(String.format(URL_PROJECT_ENVIRONMENTS, rancherUrl, projectInfo.getId()));
    }

    @Override
    public EnvironmentInfo environmentInfoByName(String projectName, String environmentName) {
        return this.environmentsByProjectName(projectName).stream()
                .filter(environmentInfo -> environmentInfo.getName().equals(environmentName))
                .findFirst().orElse(null);
    }

    @Override
    public List<ServiceInfo> servicesByEnvironmentName(String projectName, String environmentName) {
        ProjectInfo projectInfo = this.projectByName(projectName);
        EnvironmentInfo environmentInfo = this.environmentInfoByName(projectName, environmentName);
        return get(String.format(URL_PROJECT_ENVIRONMENT_SERVICES, rancherUrl, projectInfo.getId(), environmentInfo.getId()));
    }

    private <T> T get(String url) {
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        ResponseEntity<Response<T>> response = this.restTemplate.exchange(url, GET, request, new ParameterizedTypeReference<Response<T>>() {
        });
        return response.getBody().getData();
    }


    @Override
    public ServiceInfo serviceInfoByName(String projectName, String serviceName) {
        ProjectInfo projectInfo = projectByName(projectName);
        List<ServiceInfo> response = get(String.format(URL_PROJECT_SERVICES_BY_NAME, rancherUrl, projectInfo.getId(), serviceName));
        return response.stream().findFirst().orElse(null);
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
        final String requestUrl = String.format(URL_PROJECT_SERVICE, rancherUrl, serviceInfo.getAccountId(), serviceInfo.getId());
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
    public List<ServiceInstance> serviceInstances(String projectId, String serviceId) {
        return get(String.format(URL_PROJECT_SERVICE_INSTANCES, rancherUrl, projectId, serviceId));
    }

    @Override
    public List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName) {
        ServiceInfo serviceInfo = this.serviceInfoByName(projectName, serviceName);
        return this.serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId());
    }


}
