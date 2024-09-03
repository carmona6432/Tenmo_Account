package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private static String API_BASE_URL = "http://localhost:8080/";
    public void setToken(String token) {
        this.token = token;
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
            account = restTemplate.exchange(API_BASE_URL + "accounts                                  ",
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

    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        try {
            account =  restTemplate.exchange(API_BASE_URL + "accounts/user/" + accountId,
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
    public boolean updateFromAccount(Account updatedAccount) {
        boolean isUpdated = false;
        try {
            restTemplate.put(API_BASE_URL + "transfer/fromAccount/" + updatedAccount.getAccountId(),
                    makeAuthEntity(updatedAccount));
            isUpdated = true;
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return isUpdated;
    }
    public boolean updateToAccount(Account updatedAccount) {
        boolean isUpdated = false;
        try {
            restTemplate.put(API_BASE_URL + "transfer/toAccount/" + updatedAccount.getAccountId(),
                    makeAuthEntity(updatedAccount));
            isUpdated = true;
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return isUpdated;
    }
    public Account getAccountByUserId(int userId) {

        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts/" + userId,
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
    public String getUsernameByAccountId(int accountId) {

        String account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "username/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    String.class).getBody();
        } catch (ResourceAccessException e) {
            System.out.println("Error in resource access: " + e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println("API error - status code: " + e.getRawStatusCode() + ", Error message: " + e.getMessage());
        }
        return account;
    }
    private HttpEntity<Account> makeAuthEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(account, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
