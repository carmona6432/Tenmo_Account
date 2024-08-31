package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;

import java.util.List;

public interface TransferDAO {
    List<Transfer> getTransfersFromAccount(int accountId);
    List<Transfer> getTransfersToAccount(int accountId);
    Transfer getTransferByTransferId(int transferId);
    List<TransferUsername> getPendingTransfersById(int userId);
    Transfer updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
}
