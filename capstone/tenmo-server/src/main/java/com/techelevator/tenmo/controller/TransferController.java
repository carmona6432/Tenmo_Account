package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    @Autowired
    private TransferDAO transferDAO;
    private AccountDAO accountDAO;
    public TransferController(TransferDAO transferDAO, AccountDAO accountDAO){
        this.transferDAO = transferDAO;
        this.accountDAO = accountDAO;
    }
    @GetMapping(path = "transfers/{id}")
    public Transfer getTransferByTransferId(int transferId){
        return transferDAO.getTransferByTransferId(transferId);
    }

    @GetMapping(path = "transfers/from/{id}")
    public List<Transfer> getTransferFromAccount (@PathVariable int id) {
        return transferDAO.getTransfersFromAccount(id);
    }
    @GetMapping(path = "transfers/to/{id}")
    public List<Transfer> getTransferToAccount (@PathVariable int id) {
        return transferDAO.getTransfersToAccount(id);
    }
    @GetMapping(path = "transfers/pending")
    public List<Transfer> getPendingTransfersById(Principal user){
        String username = user.getName();
        return transferDAO.getPendingTransfersById(username);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfers/send")
    public void sendTransfer(@RequestBody Transfer transfer) {
        Transfer newTransfer = new Transfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
        transferDAO.createTransfer(transfer);
        accountDAO.updateAccount(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfers")
    public Transfer createTransfer(@RequestBody Transfer transfer){
       return transferDAO.createTransfer(transfer);
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "transfers/{id}")
    public Transfer updateTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        return transferDAO.updateTransfer(transfer);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transferstatus/{id}")
    public String getTransferStatusById(@PathVariable int id) {
        return transferDAO.getTransferStatusById(id);
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transfertype/{id}")
    public String getTransferTypeById(@PathVariable int id) {
        return transferDAO.getTransferTypeById(id);
    }
}
