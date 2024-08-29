package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AppController {
    @Autowired
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;

    @GetMapping(path = "accounts/{id}")
    public BigDecimal getBalance(@PathVariable int id) {
        return accountDAO.getBalance(id);
    }

    @GetMapping(path = "accounts/user/{id}")
    public List<String> accounts(@PathVariable int id){
        return accountDAO.getAccounts(id);
    }
}
