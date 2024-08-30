package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";
    public void setToken(String token) {
        this.token = token;
    }
    
    public Transfer sendTransfer() {
        
        return restTemplate.exchange(
                API_BASE_URL + "transfer",
                HttpMethod.POST,
                makeAuthEntity(),
                Transfer.class
        ).getBody();
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }


}
