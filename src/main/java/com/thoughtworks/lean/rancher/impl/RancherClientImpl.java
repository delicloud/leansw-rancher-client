package com.thoughtworks.lean.rancher.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.EnvironmentInfo;
import com.thoughtworks.lean.rancher.dto.ProjectInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import com.thoughtworks.lean.rancher.dto.request.ServiceUpdateRequest;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.*;


@Component
public class RancherClientImpl implements RancherClient {
    private final static String URL_PROJECT_ENVIRONMENTS = "%s/v1/projects/%s/environments";
    private final static String URL_PROJECT_ENVIRONMENT = "%s/v1/projects/%s/environments/%s";
    private final static String URL_PROJECT_ENVIRONMENT_SERVICES = "%s/v1/projects/%s/environments/%s/services";
    private final static String URL_PROJECT_SERVICES_BY_NAME = "%s/v1/projects/%s/services?name=%s";
    private final static String URL_PROJECT_SERVICE = "%s/v1/projects/%s/services/%s";
    private final static String URL_PROJECT_SERVICE_INSTANCES = "%s/v1/projects/%s/services/%s/instances";
    private final static String URL_PROJECT_SERVICE_INSTANCE = "%s/v1/projects/%s/containers/%s/";
    private final static String PARAM_KEY_ACTION = "action";

    private final static String URL_PROJECTS = "%s/v1/projects";

    private final static String TMPL_INSTANCE_NAME = "%s_%s_%d";

    private String accessSecret;
    private String secretKey;
    private String rancherUrl;


    private RestTemplate restTemplate;
    private int TIMEOUT = 5000;
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true);


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
        return get(String.format(URL_PROJECTS, rancherUrl),
                new TypeReference<List<ProjectInfo>>() {
                }, true);
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
    public ServiceInstance instance(String projectName, String serviceName, String containerName, int index) {
        ServiceInfo serviceInfo = serviceInfoByName(projectName, serviceName);
        List<ServiceInstance> serviceInstances = serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId());
        EnvironmentInfo envInfo = environmentInfoById(serviceInfo.getAccountId(), serviceInfo.getEnvironmentId());
        return serviceInstances.stream().filter((serviceInstance -> serviceInstance.getName().equals(String.format(TMPL_INSTANCE_NAME, envInfo.getName(), serviceName, index)))).findFirst().orElse(null);
    }

    @Override
    public List<ServiceInstance> instanceByExtPrefix(String projectName, String serviceName, String extPrefix) {
        ServiceInfo serviceInfo = serviceInfoByName(projectName, serviceName);
        List<ServiceInstance> serviceInstances = serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId());
        return serviceInstances.stream().filter(serviceInstance -> serviceInstance.getExternalId().startsWith(extPrefix)).collect(Collectors.toList());
    }

    @Override
    public List<EnvironmentInfo> environmentsByProjectName(String projectName) {
        return environmentsByProjectId(projectByName(projectName).getId());
    }

    @Override
    public <B> ServiceInstance instanceActionById(String accountId, String id, String action, B body) {
        return post(
                UriComponentsBuilder.fromHttpUrl(String.format(URL_PROJECT_SERVICE_INSTANCE, rancherUrl, accountId, id)).queryParam(PARAM_KEY_ACTION, action).build().toUriString(),
                body,
                new TypeReference<ServiceInstance>() {
                });
    }


    @Override
    public List<EnvironmentInfo> environmentsByProjectId(String projectId) {
        return get(String.format(URL_PROJECT_ENVIRONMENTS, rancherUrl, projectId),
                new TypeReference<List<EnvironmentInfo>>() {
                }, true);
    }

    @Override
    public EnvironmentInfo environmentInfoByName(String projectName, String environmentName) {
        ProjectInfo projectInfo = projectByName(projectName);
        return this.environmentsByProjectName(projectInfo.getName()).stream()
                .filter((environmentInfo) -> environmentInfo.getName().equals(environmentName))
                .findFirst().orElse(null);
    }

    @Override
    public EnvironmentInfo environmentInfoById(String projectId, String environmentId) {
        return get(String.format(URL_PROJECT_ENVIRONMENT, rancherUrl, projectId, environmentId),
                new TypeReference<EnvironmentInfo>() {
                }, false);
    }

    @Override
    public List<ServiceInfo> servicesByEnvironmentName(String projectName, String environmentName) {
        ProjectInfo projectInfo = this.projectByName(projectName);
        EnvironmentInfo environmentInfo = this.environmentInfoByName(projectName, environmentName);
        return get(String.format(URL_PROJECT_ENVIRONMENT_SERVICES, rancherUrl, projectInfo.getId(), environmentInfo.getId()),
                new TypeReference<List<ServiceInfo>>() {
                }, true);
    }


    private <T> T get(String url, TypeReference<T> tTypeReference, boolean list) {
        HttpEntity<String> request = new HttpEntity<>(buildHttpHeaders());
        ResponseEntity<String> response = this.restTemplate.exchange(url, GET, request, String.class);
        try {
            if (list) {
                JsonNode node = objectMapper.reader().readTree(response.getBody()).get("data");
                return objectMapper.readerFor(tTypeReference).readValue(node.toString());
            } else {
                return objectMapper.readerFor(tTypeReference).readValue(response.getBody());
            }
        } catch (IOException e) {
            return null;
        }
    }


    private <T, B> T post(String url, B body, TypeReference<T> tTypeReference) {

        try {
            HttpEntity<String> request;
            request = new HttpEntity<>(body == null ? "{}" : objectMapper.writer().writeValueAsString(body), buildHttpHeaders());
            ResponseEntity<String> response = this.restTemplate.exchange(url, POST, request, String.class);

            return objectMapper.readerFor(tTypeReference).readValue(response.getBody());
        } catch (IOException e) {
            return null;
        }
    }


    @Override
    public ServiceInfo serviceInfoByName(String projectName, String serviceName) {
        ProjectInfo projectInfo = projectByName(projectName);
        List<ServiceInfo> response = get(String.format(URL_PROJECT_SERVICES_BY_NAME, rancherUrl, projectInfo.getId(), serviceName),
                new TypeReference<List<ServiceInfo>>() {
                }, true);
        assert response != null;
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
        return get(String.format(URL_PROJECT_SERVICE_INSTANCES, rancherUrl, projectId, serviceId),
                new TypeReference<List<ServiceInstance>>() {
                }, true);
    }

    @Override
    public List<ServiceInstance> serviceInstancesByName(String projectName, String serviceName) {
        ServiceInfo serviceInfo = this.serviceInfoByName(projectName, serviceName);
        return this.serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId());
    }


}
