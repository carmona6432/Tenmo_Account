package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.login.AuthenticatedUser;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private static String API_BASE_URL = "http://localhost:8080/";

    public void setToken(String token) {
        this.token = token;
    }

    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
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

    public Transfer sendTransfer(Transfer sent) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
                    API_BASE_URL + "transfers/send",
                    HttpMethod.POST,
                    makeAuthEntity(sent),
                    Transfer.class
            ).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfer;
    }
    public Transfer sendRequest(Transfer request) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
                    API_BASE_URL + "transfers/request",
                    HttpMethod.POST,
                    makeAuthEntity(request),
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

    public String getTransferStatusById(int transferStatusId) {
        String transferStatus = null;
        try {
            String url = API_BASE_URL + "/transferstatus/" + transferStatusId;
            transferStatus = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),
                    String.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }

        return transferStatus;
    }

    public String getTransferTypeById(int transferTypeId) {
        String transferType = null;
        try {
            String url = API_BASE_URL + "/transfertype/" + transferTypeId;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),
                    String.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferType;
    }

    public List<Transfer> getPendingTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfers/pending/",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    List.class
                    ).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfers;
    }

    public Transfer[] getTransfersFromAccount(int id) {
        Transfer[] transfer = null;
        try {
            transfer =  restTemplate.exchange(API_BASE_URL + "transfers/from/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfer;
    }

    public Transfer[] getTransfersToAccount(int id){
        Transfer[] transferUsername = null;
        try {
            transferUsername = restTemplate.exchange(API_BASE_URL + "transfers/to/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferUsername;
    }

    public List<Transfer> getPendingRequests(int id) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfers/pending/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    List.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transfers;
    }

    private HttpEntity<Transfer> makeAuthEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity () {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
            }
    }

