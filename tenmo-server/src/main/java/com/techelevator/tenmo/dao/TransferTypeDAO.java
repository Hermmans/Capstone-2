package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

import java.util.List;

public interface TransferTypeDAO {
    public List<TransferType> getAllTranferTypes();
    public String getTransferedType(long id);
}
