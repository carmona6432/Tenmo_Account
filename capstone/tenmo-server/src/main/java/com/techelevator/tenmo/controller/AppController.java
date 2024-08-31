package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
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
    @Autowired
    private TransferTypeDAO transferTypeDAO;
    @Autowired
    private TransferStatusDAO transferStatusDAO;

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
    @GetMapping(path="/transfertype/sort")
    public TransferType getTransferTypeByDescription(@RequestParam String description) {
       return transferTypeDAO.getTransferTypeByDescription(description);
    }
    @GetMapping(path="/transferstatus/sort")
    public TransferStatus getTransferStatusByDescription(@RequestParam String description) {
        return transferStatusDAO.getTransferStatusByDescription(description);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transferstatus/{id}")
    public String getTransferStatusById(@PathVariable int id) {
        return transferStatusDAO.getTransferStatusById(id);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transfertype/{id}")
    public String getTransferTypeById(@PathVariable int id) {
        return transferTypeDAO.getTransferTypeById(id);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path = "username/{id}")
    String getUsernameByAccountId(@PathVariable int account_id){
        return accountDAO.getUsernameByAccountId(account_id);
    }


}
