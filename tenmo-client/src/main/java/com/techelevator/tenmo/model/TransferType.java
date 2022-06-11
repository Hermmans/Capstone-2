package com.techelevator.tenmo.model;

public class TransferType {

    private Long typeId;
    private String typeDesc;

    public TransferType(Long typeId, String typeDesc) {
        this.typeId = typeId;
        this.typeDesc = typeDesc;
    }

    public TransferType() {
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}