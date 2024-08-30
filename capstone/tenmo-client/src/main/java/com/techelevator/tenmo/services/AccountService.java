package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;
import com.techelevator.tenmo.model.login.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";
    public void setToken(String token) {
        this.token = token;
    }
    public BigDecimal getBalance() {
        BigDecimal bigD = null;
        try {
            bigD =  restTemplate.exchange(API_BASE_URL + "balance",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    BigDecimal.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return bigD;
    }
    public Account[] getAccounts() {
        Account[] accounts = new Account[0];
        try {
            accounts = restTemplate.exchange(API_BASE_URL + "accounts/users",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return accounts;
    }
    public Account getAccount(){
        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return account;
    }
    public TransferUsername[] getTransfersFromAccount(int id) {
        TransferUsername[] transferUsername = null;
        try {
            transferUsername =  restTemplate.exchange(API_BASE_URL + "transfers/from/" + id,
                HttpMethod.GET,
                makeAuthEntity(),
                TransferUsername[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferUsername;
    }

    public TransferUsername[] getTransfersToAccount(int id){
        TransferUsername[] transferUsername = null;
        try {
           transferUsername = restTemplate.exchange(API_BASE_URL + "transfers/to/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    TransferUsername[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferUsername;
    }

    public TransferUsername[] getPendingRequests(int id) {
        TransferUsername[] transferUsername = null;
        try {
            transferUsername = restTemplate.exchange(API_BASE_URL + "transfers/pending/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    TransferUsername[].class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return transferUsername;
    }

    public Account getAccountByUserId(int userId) {

        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts/user/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return account;
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
