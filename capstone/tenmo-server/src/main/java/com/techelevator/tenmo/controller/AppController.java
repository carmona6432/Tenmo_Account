package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    TransferTypeDAO transferTypeDAO;
    @Autowired
    TransferStatusDAO transferStatusDAO;

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
            public List<Transfer> getTransferFromAccount (Principal user) {
        String username = user.getName();
        return transferDAO.getTransferFromAccount(username);
    }
    @GetMapping(path = "transfer/to")
    public List<Transfer> getTransferToAccount (Principal user) {
        String username = user.getName();
        return transferDAO.getTransferToAccount(username);
    }
    @GetMapping(path="/transfertype/sort")
    public TransferType getTransferTypeByDescription(@RequestParam String description) {
       return transferTypeDAO.getTransferTypeByDescription(description);
    }
    @GetMapping(path="/transferstatus/sort")
    public TransferStatus getTransferStatusByDescription(@RequestParam String description) {
        return transferStatusDAO.getTransferStatusByDescription(description);
    }
    @GetMapping(path="transferstatus/{id}")
    public TransferStatus getTransferStatusById(@PathVariable int id) {
        return transferStatusDAO.getTransferStatusById(id);
    }
    @GetMapping(path="transfertype/{id}")
    public TransferType getTransferTypeById(@PathVariable int id) {
        return transferTypeDAO.getTransferTypeById(id);
    }

}
