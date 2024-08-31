package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeDAO {
    
    TransferType getTransferTypeByDescription(String description);
    
    String getTransferTypeById(int transferId);
}
