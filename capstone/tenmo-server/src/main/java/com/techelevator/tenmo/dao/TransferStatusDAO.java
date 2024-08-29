package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDAO {
    
    TransferStatus getTransferStatusByDescription(String description);
    
    TransferStatus getTransferStatusById(int transferStatusId);
    
    
}
