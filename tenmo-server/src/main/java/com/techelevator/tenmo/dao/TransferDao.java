package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetail;

public interface TransferDao {
    Transfer[] getAllTransfers();

    Transfer[] getTransfersByUserId(Long id);

    void updateTransfer(Transfer transfer, Long typeId, Long statusId, Long transferId);

    Transfer getTransferByTransferId(Long id);

    TransferDetail[] getTransferDetails(Long id);

    Transfer addTransfer(Transfer transfer, Long statusId, Long statusTypeId, Long idFrom, Long idTo, Double amount);

//    TransferDetail getTransferSum(Long id);

}
