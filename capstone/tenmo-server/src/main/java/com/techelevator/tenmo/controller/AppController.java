package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AppController {
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping(path = "/accounts")
    public Account getAccount(Principal user) {
        String username = user.getName();
        return accountDAO.getAccount(username);
    }

    @GetMapping(path = "accounts/{userId}")
    public Account getAccountByUserId(@PathVariable int userId) {
        return accountDAO.getAccountByUserId(userId);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path = "accounts/user/{accountId}")
    public Account getAccountByAccountId(@PathVariable int accountId){
        return accountDAO.getAccountByAccountId(accountId);
    }

    @GetMapping(path = "accounts/users")
    public List<Account> accounts(Principal user) {
        String username = user.getName();
        return accountDAO.getAccounts(username);
    }
    @PutMapping(path = "transfer/fromAccount/{id}")
    public Account updateFromAccount(@PathVariable int id, @RequestBody Account account) {
        account.setAccountId(id);
        try {
            Account updatedAccount = accountDAO.updateFromAccount(account);
            return updatedAccount;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found.");
        }
    }
    @PutMapping(path = "transfer/toAccount/{id}")
    public Account updateToAccount(@PathVariable int id, @RequestBody Account account) {
        account.setAccountId(id);
        try {
            Account updatedAccount = accountDAO.updateToAccount(account);
            return updatedAccount;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found.");
        }
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "/username/{id}")
    public String getUsernameByUserId(@PathVariable int id) {
        return accountDAO.getUsernameByAccountId(id);
    }
}