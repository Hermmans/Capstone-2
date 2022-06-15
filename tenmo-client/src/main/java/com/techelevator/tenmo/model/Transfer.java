package com.techelevator.tenmo.model;

import com.techelevator.tenmo.services.ConsoleService;

public class Transfer {

    private Long transferId;
    private Long transferTypeId;
    private String transferTypeDesc;
    private Long transferStatusId;
    private String trasferStatusDesc;

    private Long accountFrom;
    private Long accountTo;
    private Double amount;

    public Transfer(Long transferId, Long transferTypeId, String transferTypeDesc, Long transferStatusId, String trasferStatusDesc, Long accountFrom, Long accountTo, Double amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferTypeDesc = transferTypeDesc;
        this.transferStatusId = transferStatusId;
        this.trasferStatusDesc = trasferStatusDesc;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    private final ConsoleService consoleService = new ConsoleService();


    public String toString(TransferType transferType, TransferStatus transferStatus) {
        return "-------------------- \n" +
                "Transfer Details\n" +
                "--------------------\n" +
                "Id: " + transferId +
                "\nFrom: " + accountFrom +
                "\nTo: " + accountTo +
                "\nAmount: " + consoleService.printPrettyMoney(getAmount()) +
                "\nType: " +transferType.getTypeDesc() +
                "\nStatus: "+transferStatus.getTrasferStatusDesc();
    }

    public Transfer() {
    }

    public String getTrasferStatusDesc() {
        return trasferStatusDesc;
    }

    public void setTrasferStatusDesc(String trasferStatusDesc) {
        this.trasferStatusDesc = trasferStatusDesc;
    }

    public String getTypeDesc() {
        return transferTypeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.transferTypeDesc = typeDesc;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}