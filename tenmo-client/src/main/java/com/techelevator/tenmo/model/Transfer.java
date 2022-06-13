package com.techelevator.tenmo.model;

import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.ConsoleService;

public class Transfer {

    private Long transferId;

    private Long accountFrom;
    private Long accountTo;
    private Double amount;

    private Long transferStatusId;
    private String trasferStatusDesc;

    private Long transferTypeId;
    private String typeDesc;

    private final ConsoleService consoleService = new ConsoleService();

    @Override
    public String toString() {
        return "-------------------- \n" +
                "Transfer Details\n" +
                "--------------------\n" +
                "Id: " + transferId +
                "\nFrom: " + accountFrom +
                "\nTo: " + accountTo +
                "\nAmount: " + consoleService.printPrettyMoney(getAmount()) +
                "\nType: " +this.typeDesc +
                "\nStatus: "+this.trasferStatusDesc;
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
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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