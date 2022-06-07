package com.techelevator.tenmo.model;

import javax.persistence.*;

@Table(name = "transfer_type")
public class TransferType {

    @Id
    @SequenceGenerator(
            name = "transfer_type_sequence",
            sequenceName = "transfer_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transfer_type_sequence"
    )
    private Long transferTypeId;


    private String transferTypeDescription;



    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public String getTransferTypeDescription() {
        return transferTypeDescription;
    }
}
