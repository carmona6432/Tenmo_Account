package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    public Transfer getTransfer();
    List<Transfer> getTransfersById(int userId);
    Transfer getTransferByTransferId(int transferId);
    List<Transfer> getPendingTransfersById(int userId);
    void updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
    
    //May need a get all method
}
