package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AppController {
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private TransferDAO transferDAO;

    @GetMapping(path = "balance")
    public BigDecimal getBalance(Principal user) {
        String username = user.getName();
        return accountDAO.getBalance(username);
    }

    @GetMapping(path = "accounts/user/{id}")
    public List<String> accounts(@PathVariable int id){
        return accountDAO.getAccounts(id);
    }

    @GetMapping(path = "transfer/from")
            public List<Transfer> getTransfersFromAccount (Principal user) {
        String username = user.getName();
        return transferDAO.getTransfersFromAccount(username);
    }
    @GetMapping(path = "transfer/to")
    public List<Transfer> getTransfersToAccount (Principal user) {
        String username = user.getName();
        return transferDAO.getTransfersToAccount(username);
    }
}
