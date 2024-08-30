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

    @GetMapping(path = "accounts")
    public Account getAccount(Principal user){
        String username = user.getName();
        return accountDAO.getAccount(username);
    }
    @GetMapping(path = "accounts/{id}")
    public Account getAccountByUserId(int user_id){
        return accountDAO.getAccountByUserId(user_id);
    }
    @GetMapping(path = "accounts/users")
    public List<Account> accounts(Principal user){
        String username = user.getName();
        return accountDAO.getAccounts(username);
    }
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "accounts/{accountId}")
    void updateAccount(@RequestBody Account account, int amount, @PathVariable int from_account, int to_account){
        accountDAO.updateAccount(account,amount,from_account,to_account);
    }
    @GetMapping(path = "transfers/{id}")
    public Transfer getTransferByTransferId(int transferId){
        return transferDAO.getTransferByTransferId(transferId);
    }

    @GetMapping(path = "transfers/from/{id}")
    public List<TransferUsername> getTransferFromAccount (@PathVariable int id) {
        return transferDAO.getTransfersFromAccount(id);
    }
    @GetMapping(path = "transfers/to/{id}")
    public List<TransferUsername> getTransferToAccount (@PathVariable int id) {
        return transferDAO.getTransfersToAccount(id);
    }
    @GetMapping(path = "transfers/pending/{id}")
    public List<TransferUsername> getPendingTransfersById(@PathVariable int id){
        return transferDAO.getPendingTransfersById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfers")
    public void createTransfer(@RequestBody Transfer transfer){
        createTransfer(transfer);
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "transfers/{id}")
    public Transfer updateTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        return transferDAO.updateTransfer(transfer);
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
