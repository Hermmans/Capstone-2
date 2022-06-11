package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
    Transfer[] getAllTransfers();


    Transfer[] getTransfersByUserId(Long id);
}
