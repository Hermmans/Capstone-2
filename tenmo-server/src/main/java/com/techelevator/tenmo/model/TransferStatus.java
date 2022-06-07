package com.techelevator.tenmo.model;

import javax.persistence.*;

@Table(name = "transfer_status")
public class TransferStatus {

    @Id
    @SequenceGenerator(
            name = "transfer_status_sequence",
            sequenceName = "transfer_status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transfer_status_sequence"
    )
    private Long transferStatusId;


    private String transferStatusDescription;


    public TransferStatus() {
    }


    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

}
