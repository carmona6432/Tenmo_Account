package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;

import java.util.List;

public interface TransferDAO {

    List<TransferUsername> getTransferFromAccount(int accountId);
    List<TransferUsername> getTransferToAccount(int accountId);

    Transfer getTransferByTransferId(int transferId);
    List<TransferUsername> getPendingTransfersById(String username);
    void updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
    
    //May need a get all method
}
