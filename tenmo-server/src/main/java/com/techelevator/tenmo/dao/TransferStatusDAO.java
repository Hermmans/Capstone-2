package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

import java.util.List;

public interface TransferStatusDAO {
    public List<TransferStatus> getAllTransfer();
    public String getTransferStatus(long id);
}
