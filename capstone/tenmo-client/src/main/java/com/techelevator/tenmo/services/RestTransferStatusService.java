package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestTransferStatusService implements TransferStatusService {
    private final String API_BASE_URL; //="http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();
    public RestTransferStatusService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    @Override
    public TransferStatus getTransferStatus(AuthenticatedUser authenticatedUser, String description) {
        TransferStatus transferStatus = null;
        try {
            String url = API_BASE_URL + "/transferstatus/sort?description=" + description;
            transferStatus = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser),
                    TransferStatus.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferStatus;
    }
    @Override
    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int transferStatusId) {
        TransferStatus transferStatus = null;
        try {
            String url = API_BASE_URL + "/transferstatus/" + transferStatusId;
            transferStatus = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser),
                    TransferStatus.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferStatus;
    }
    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return httpEntity;
    }
}
