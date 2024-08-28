package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping(path = "accounts/{id}")
    public double getBalance(@PathVariable int id) {
        return accountDAO.getBalance(id);
    }
}
