package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDAO accountDAO;
    private UserDao userDAO;


    @Autowired
    public AccountController(AccountDAO accountDAO, UserDao userDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    //END POINTS that have methods associated with them

    @PreAuthorize("permitAll")
    @GetMapping(path = "accounts/")
    public Account[] findAllAccounts(){
        return accountDAO.findAllAccounts();
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "account/user/{id}")
    public Account getByUserId(@PathVariable Long id){
        return accountDAO.findByAccountId(id);
    }

    @PreAuthorize("permitAll")
    @PutMapping(path = "withdraw/{id}/{amount}")
    public Account withdraw(@Valid @RequestBody Account account, @PathVariable Long id, @PathVariable Double amount){
        Account account1 = accountDAO.withdrawAcct(account, id, amount);
        return account1;
    }

    @PreAuthorize("permitAll")
    @PutMapping(path = "deposit/{id}/{amount}")
    public Account deposit(@Valid @RequestBody Account account, @PathVariable Long id,@PathVariable Double amount){
        Account account1 = accountDAO.depositAcct(account, id, amount);
        return account1;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable Long id) {
        return accountDAO.getBalance(id);
    }

    //////////////////////////////





    //some testing
    //we have a getBalance path ^
    @PreAuthorize("permitAll")
    @RequestMapping(path = "balance_object/{id}", method = RequestMethod.GET)
    public long getBalance(@PathVariable int id) {
        return accountDAO.getBalanceObject(id);
    }
    //some more testing just to see if it works

    //I modified this to [withdraw();]
    //it needed to return an object, and we needed a putmapping for updates
    @PreAuthorize("permitAll")
    @GetMapping(path = "add/{id}/{amount}")
    public long addToBalance(@PathVariable long id, @PathVariable long amount){
        return accountDAO.addToBalance(amount, id);
}
    //some more testing just to see if it works
//    @PreAuthorize("permitAll")
//    @GetMapping(path = "withdraw/{id}/{amount}")
//    public long withDrawToBalance(@PathVariable long id, @PathVariable long amount) {
//        return accountDAO.withdraw(amount, id);
//    }
//    @PreAuthorize("permitAll")
//    @RequestMapping(path = "listusers", method = RequestMethod.GET)
//    public List<User> listUsers() {
//        return userDAO.findAll();
//    }

//    @PreAuthorize("permitAll")
//    @PutMapping(path = "sendmoney/{id}/{id}")
//    public Account sendMoney(@Valid @RequestBody Account account, @PathVariable Long from, @PathVariable Long to){
//        return accountDAO.sendMoney(account, from, to);
//    }
//
//    @PreAuthorize("permitAll")
//    @GetMapping(path = "account/user/{id}")
//    public Account getByUserId(@PathVariable Long id){
//        return accountDAO.getAccountByUserId(id);
//    }
//
//    @PreAuthorize("permitAll")
//    @GetMapping(path = "account/{id}")
//    public Account getByAccountId(@PathVariable Long id){
//        return accountDAO.getAccountByUserId(id);
//    }

}
