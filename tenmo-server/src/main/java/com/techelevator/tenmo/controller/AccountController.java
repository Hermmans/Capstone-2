package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDAO accountDAO;
    private UserDao userDAO;

    public AccountController(AccountDAO accountDAO, UserDao userDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "accounts/")
    public Account[] findAllAccounts(){
        return accountDAO.findAllAccounts();
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "account/user/{id}")
    public Account getByUserId(@PathVariable Long id){
        return accountDAO.findByUserId(id);
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "account/{id}")
    public Account getByAccountId(@PathVariable Long id){
        return accountDAO.findByAccountId(id);
    }

    @PreAuthorize("permitAll")
    @PutMapping(path = "withdraw/{id}/{amount}")
    public Account withdraw(@Valid @RequestBody Account account, @PathVariable Long id, @PathVariable Double amount){
        return accountDAO.withdrawAcct(account, id, amount);
    }

    @PreAuthorize("permitAll")
    @PutMapping(path = "deposit/{id}/{amount}")
    public Account deposit(@Valid @RequestBody Account account, @PathVariable Long id,@PathVariable Double amount){
        return accountDAO.depositAcct(account, id, amount);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable Long id) {
        return accountDAO.getBalance(id);
    }


}
