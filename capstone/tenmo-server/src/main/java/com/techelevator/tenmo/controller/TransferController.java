package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    @Autowired
    private TransferDAO transferDAO;
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
        transferDAO.createTransfer(transfer);
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "transfers/{id}")
    public Transfer updateTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        return transferDAO.updateTransfer(transfer);
    }
}
