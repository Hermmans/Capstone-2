package com.techelevator.tenmo.model;

public class Account {

    private Long accountId;
    private Long userId;
    private Double balance;

    //maybe
    //account to user has ManyToOne users
    private User user = new User();

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
