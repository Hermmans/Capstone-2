package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public AccountService(String URL, AuthenticatedUser user) {
        this.authenticatedUser = user;
        this.API_BASE_URL = URL;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }


    //BASIC CRUD OPERATIONS

    //maybe account/user/balance/id?
    public Account getBalance(Long id) {
        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "balance/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class).getBody();

        } catch (RestClientResponseException rcre) {
            BasicLogger.log(rcre.getRawStatusCode() + " : " + rcre.getStatusText());
        } catch (ResourceAccessException rae) {
            BasicLogger.log(rae.getMessage());
        }
        return account;
    }


    public User[] getUsers(){
        User[] users = null;
        try{
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL+"users/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            User[].class);
            users = response.getBody();
        }catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

}