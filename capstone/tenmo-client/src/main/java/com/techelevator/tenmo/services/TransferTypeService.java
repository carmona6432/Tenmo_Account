package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.login.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferTypeService {
    private final String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    public TransferTypeService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    public TransferType getTransferType(AuthenticatedUser authenticatedUser, String description) {
        TransferType transferType = null;
        try {
            String url = API_BASE_URL + "/transfertype/sort?description=" + description;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser),
                    TransferType.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferType;
    }

    public TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, int transferTypeId) {
        TransferType transferType = null;
        try {
            String url = API_BASE_URL + "/transfertype/" + transferTypeId;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser),
                    TransferType.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferType;
    }
    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return httpEntity;
    }
}
