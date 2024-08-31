package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.login.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";
    public void setToken(String token) {
        this.token = token;
    }
    public Transfer transferById(int id){
        Transfer transfer = null;
        try{transfer = restTemplate.exchange(
                API_BASE_URL + "transfers/" + id,
                HttpMethod.GET,
                makeAuthEntity(),
                Transfer.class
        ).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfer;
    }
    public Transfer sendTransfer() {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
                    API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    makeAuthEntity(),
                    Transfer.class
                    ).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfer;
    }
    public boolean updateTransfer(Transfer transfer) {
        boolean isUpdated = false;
        try {
            restTemplate.put(API_BASE_URL +
                "transfers/" +
                transfer.getId(),
                makeAuthEntity());
                isUpdated = true;
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return isUpdated;
    }
    public TransferStatus getTransferStatusById(int transferStatusId) {
        TransferStatus transferStatus = null;
        try {
            String url = API_BASE_URL + "/transferstatus/" + transferStatusId;
            transferStatus = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),
                    TransferStatus.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }

        return transferStatus;
    }
    public TransferType getTransferTypeById(int transferTypeId) {
        TransferType transferType = null;
        try {
            String url = API_BASE_URL + "/transfertype/" + transferTypeId;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),
                    TransferType.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferType;
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
