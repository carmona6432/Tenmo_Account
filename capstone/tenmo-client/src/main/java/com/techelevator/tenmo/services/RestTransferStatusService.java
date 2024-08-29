package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;

public class RestTransferStatusService implements TransferStatusService {
    @Override
    public TransferStatus getTransferStatus(AuthenticatedUser authenticatedUser, String description) {
        return null;
    }

    @Override
    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int id) {
        return null;
    }
}
