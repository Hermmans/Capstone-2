package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDAO {

     TransferStatus[] getAllTransferStatus();

     TransferStatus getTransferStatus(Long id);

}
