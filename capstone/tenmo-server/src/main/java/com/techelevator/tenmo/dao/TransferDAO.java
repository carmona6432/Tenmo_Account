package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    List<Transfer> getTransfersFromAccount(String username);
    List<Transfer> getTransfersToAccount(String username);
    List<Transfer> getPendingTransfersById(int userId);
    void updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
    
    //May need a get all method
}
