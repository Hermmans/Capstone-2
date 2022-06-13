package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeDAO {

    TransferType[] getAllTranferTypes();

    TransferType getTransfereTypeById(Long id);

}
