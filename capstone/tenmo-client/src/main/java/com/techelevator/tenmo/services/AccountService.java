package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";
    public void setToken(String token) {
        this.token = token;
    }
    public BigDecimal getBalance() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
         return restTemplate.exchange(API_BASE_URL + "/balance",
                 HttpMethod.GET,
                 entity,
                 BigDecimal.class).getBody();
    }
    public Transfer[] getTransferFromAccount () {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "transfer/from",
                HttpMethod.GET,
                entity,
                Transfer[].class).getBody();}

    public  Transfer[] getTransferToAccount(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "transfer/to",
                HttpMethod.GET,
                entity,
                Transfer[].class).getBody();
    }
}
