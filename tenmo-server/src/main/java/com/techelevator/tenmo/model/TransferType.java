package com.techelevator.tenmo.model;

public class TransferType {
    private long transfer_type_id;
    private String transfer_type_desc;

    public long getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(long transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public void setTransfer_type_desc(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }
}
