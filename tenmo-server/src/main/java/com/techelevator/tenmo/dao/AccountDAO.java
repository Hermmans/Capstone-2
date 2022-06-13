package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

    Account getBalance(Long userId);

    Account[] findAllAccounts();

    Account withdrawAcct(Account account, Long id, Double amount);

    Account depositAcct(Account account, Long id, Double amount);

    Account findByAccountId(Long id);

}
