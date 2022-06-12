package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


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

    private HttpEntity<Account> makeTransferEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(account, headers);
    }

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

    public Account[] listAccounts(){
        Account[] accounts = null;
        try{
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(API_BASE_URL+"accounts/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Account[].class);
            accounts=response.getBody();
        }catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    public User[] getUsers(){
        User[] users = null;
        try{
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL+"user/",
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

    //this updates the balance and returns an account with the new balance
    public Account withdraw(Long id, Double amount){
        Account accountFrom = new Account();
        Account[] allAccounts = listAccounts();
        Double balance = 0D;
        for(Account acct: allAccounts){
            if(acct.getUserId().equals(id)){
                accountFrom = acct;
                balance = acct.getBalance();
            }

            accountFrom.setBalance(balance-amount);
        }

        return accountFrom;
    }
    //this sends a PUT with the new balance post withdrawal
    public void updateWithdraw(Account withdrawnAccount, Double amount){
        HttpEntity<Account> entity = makeTransferEntity(withdrawnAccount);
        try{
            restTemplate.put(API_BASE_URL+"withdraw/"+withdrawnAccount.getUserId()+"/"+amount,
                    entity);
        }catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    //this updates the balance and returns an account with the new balance
    public Account deposit(Long id, Double amount){
        Account accountTo = new Account();
        Account[] allAccounts = listAccounts();
        Double balance = 0D;
        for(Account acct: allAccounts){
            if(acct.getUserId().equals(id)){
                accountTo = acct;
                balance= acct.getBalance();
            }
            accountTo.setBalance(balance+amount);
        }
        return accountTo;
    }

    //this sends a PUT with the new balance post deposit
    public void updateDeposit(Account depositAccount, Double amount){
        HttpEntity<Account> entity = makeTransferEntity(depositAccount);
        try{
            restTemplate.put(API_BASE_URL+"withdraw/"+depositAccount.getUserId()+"/"+amount,
                    entity);
        }catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    //I wanna clean up the app side of how it all prints
    //sendBucks is a bit long
    public void handleSendBucksPrint(){
        System.out.println("Choose the username of the bitch you wanna send money to");
        System.out.println("---------------------------------------------------------");
        System.out.println("Users");
    }




}