package com.techelevator.tenmo.model;

public class TransferStatus {

    private Long transferStatusId;
    private String trasferStatusDesc;

    public TransferStatus(Long statusId, String statusDesc) {
        this.transferStatusId = statusId;
        this.trasferStatusDesc = statusDesc;
    }

    public TransferStatus() {
    }


    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTrasferStatusDesc() {
        return trasferStatusDesc;
    }

    public void setTrasferStatusDesc(String trasferStatusDesc) {
        this.trasferStatusDesc = trasferStatusDesc;
    }
}