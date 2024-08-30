package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;

import java.util.List;

public interface TransferDAO {
    List<TransferUsername> getTransfersFromAccount(int accountId);
    List<TransferUsername> getTransfersToAccount(int accountId);
    public Transfer getTransferByTransferId(int transferId);
    List<TransferUsername> getPendingTransfersById(int userId);
    void updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
    
    //May need a get all method
}
