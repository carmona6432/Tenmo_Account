package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {
    List<Transfer> getTransfersFromAccount(int accountId);
    List<Transfer> getTransfersToAccount(int accountId);
    Transfer getTransferByTransferId(int transferId);
    List<Transfer> getPendingTransfers(int accountFrom);

    Transfer updateTransfer(Transfer transfer);
    void createTransfer(Transfer transfer);
    int getTransferIdLimitOne();
    String getTransferStatusById(int transferStatusId);
    String getTransferTypeById(int transferId);
}
