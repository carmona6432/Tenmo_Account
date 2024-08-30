package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
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
    @Autowired
    private TransferDAO transferDAO;
    @Autowired
    private TransferTypeDAO transferTypeDAO;
    @Autowired
    private TransferStatusDAO transferStatusDAO;

    @GetMapping(path = "account")
    public Account getAccount(Principal user){
        String username = user.getName();
        return accountDAO.getAccount(username);
    }

    @GetMapping(path = "accounts/users")
    public List<Account> accounts(Principal user){
        String username = user.getName();
        return accountDAO.getAccounts(username);
    }

    @GetMapping(path = "transfer/from/{id}")
    public List<TransferUsername> getTransferFromAccount (@PathVariable int id) {
        return transferDAO.getTransfersFromAccount(id);
    }
    @GetMapping(path = "transfer/to/{id}")
    public List<TransferUsername> getTransferToAccount (@PathVariable int id) {
        return transferDAO.getTransfersToAccount(id);
    }
    @GetMapping(path = "transfer/pending/{id}")
    public List<TransferUsername> getPendingTransfersById(@PathVariable int id){
        return transferDAO.getPendingTransfersById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfers")
    public void createTransfer(Transfer transfer){
        createTransfer(transfer);
    }

    @PutMapping(path = "transfers/{id}")
    public void updateTransfer(){
        updateTransfer();
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
