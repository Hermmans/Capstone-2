package com.techelevator.tenmo.model;

import javax.persistence.*;

@Table(name = "account")
public class Account {


    public Account() {
    }

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;

    @Column(
            name = "user_id",
            nullable = false
    )

    @JoinTable(
            name = "tenmo_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Long userId;

    @Column(
            name = "balance",
            nullable = false,
            precision = 13, scale = 2
    )
    private double balance;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
