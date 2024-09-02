package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AppController {
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping(path = "/accounts")
    public Account getAccount(Principal user){
        String username = user.getName();
        return accountDAO.getAccount(username);
    }
    @GetMapping(path = "accounts/{userId}")
    public Account getAccountByUserId(@PathVariable int userId){
        return accountDAO.getAccountByUserId(userId);
    }
    @GetMapping(path = "accounts/users")
    public List<Account> accounts(Principal user){
        String username = user.getName();
        return accountDAO.getAccounts(username);
    }
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "accounts/transfer")
     public void updateBalance(BigDecimal amount, int fromAccount, int toAccount){
        accountDAO.updateBalance(amount,fromAccount,toAccount);
    }

    @PreAuthorize("permitAll")
    @GetMapping(path ="/username/{id}")
    public String getUsernameByUserId(@PathVariable int id){
        return accountDAO.getUsernameByUserId(id);
    }
    @PostMapping(path = "accounts/{id}")
    public Account updateAccount(Account account) {
        return accountDAO.updateAccount(account);
    }
}
