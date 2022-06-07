package com.techelevator.tenmo.model;

import javax.persistence.*;

@Table(name = "transfer")
public class Transfer {

    @Id
    @SequenceGenerator(
            name = "transfer_sequence",
            sequenceName = "transfer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transfer_sequence"
    )
    private Long transferId;

    @JoinTable(
            name = "transfer_type",
            joinColumns = @JoinColumn(name = "transfer_type"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id")
    )
    private Long transferType;

    @JoinTable(
            name = "transfer_status",
            joinColumns = @JoinColumn(name = "transfer_status"),
            inverseJoinColumns = @JoinColumn(name = "transfer_status_id")
    )
    private Long transferStatus;




    @ManyToOne
    @JoinTable(
            name = "account",
            joinColumns = @JoinColumn(name = "account_from"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Long accountFrom;

    @ManyToOne
    @JoinTable(
            name = "account",
            joinColumns = @JoinColumn(name = "account_to"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Long accountTo;


    private Long amount;










    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferType() {
        return transferType;
    }

    public void setTransferType(Long transferType) {
        this.transferType = transferType;
    }

    public Long getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Long transferStatus) {
        this.transferStatus = transferStatus;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
