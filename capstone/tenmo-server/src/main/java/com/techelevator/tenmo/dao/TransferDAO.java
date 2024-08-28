package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

    public Transfer getTransfer();
    public void updateToUser(); //Updates balance
    public void updateFromUser(); //Updates balance

}
