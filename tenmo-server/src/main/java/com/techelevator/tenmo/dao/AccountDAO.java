package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountDAO {

    //works
     Account getBalance(Long userId);

    public long getBalanceObject(long id);
    public void updateAccount(Account account) ;
    public long addToBalance(Long amountToAdd, Long accountId);

   public long withdraw(Long amountToSub, Long accountId);

    Account findByAccountId(Long accountId);


}
