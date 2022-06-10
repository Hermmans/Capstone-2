package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountDAO {

    //works
     Account getBalance(Long userId);


    Long addToBalance(Long amountToAdd, Long accountId);

    boolean withdraw(Long amountToSub, Long accountId);

    Account findByAccountId(Long accountId);


}
