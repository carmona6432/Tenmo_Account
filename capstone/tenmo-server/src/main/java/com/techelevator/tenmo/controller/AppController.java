package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AppController {
    @Autowired
    private AccountDAO accountDAO;
//    private UserDAO userDAO;

    @GetMapping(path = "accounts/{id}")
    public BigDecimal getBalance(@PathVariable int id) {
        return accountDAO.getBalance(id);
    }
}
