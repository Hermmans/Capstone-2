package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize("isAuthenticated()")
public class  TransferController {

        private TransferStatusDAO transferStatusDAO;
        private TransferDao transferDao;
        private TransferTypeDAO transferTypeDAO;


        public  TransferController( TransferStatusDAO transferStatusDAO, TransferDao transferDao, TransferTypeDAO transferTypeDAO) {
            this.transferStatusDAO = transferStatusDAO;
            this.transferDao = transferDao;
            this.transferTypeDAO = transferTypeDAO;
        }


    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "transfers/{id}")
    public Transfer[] listTransferById(@PathVariable Long id) {
        return transferDao.getTransfersByUserId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "trans/{id}")
    public Transfer listTransferByTransferId(@PathVariable Long id) {
        return transferDao.getTransferByTransferId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "transfers/")
    public Transfer[] listAllTransfers(){
        return transferDao.getAllTransfers();
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "transfer/update/{typeId}/{statusId}/{transferId}")
    public void update(@Valid @RequestBody Transfer transfer,
                            @PathVariable Long typeId,
                            @PathVariable Long statusId,
                            @PathVariable Long transferId){
        transferDao.updateTransfer(transfer, typeId, statusId, transferId);
    }

    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfer/{statusId}/{statusTypeId}/{idFrom}/{idTo}/{amount}")
    public Transfer addToTransfer(@Valid @RequestBody Transfer transfer,
                                      @PathVariable Long statusId,
                                      @PathVariable Long statusTypeId,
                                      @PathVariable Long idFrom,
                                      @PathVariable Long idTo,
                                      @PathVariable Double amount){
        return transferDao.addTransfer(transfer, statusId, statusTypeId, idFrom, idTo, amount);
    }

    //TRANSFER DETAILS
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "transferdetails/{id}")
    public TransferDetail[] listTransferDetails(@PathVariable Long id){
        return transferDao.getTransferDetails(id);
    }

//    @PreAuthorize("permitAll")
//    @GetMapping(path="transferdetail/sum/{id}")
//    public TransferDetail getTransferSum(@PathVariable Long id){
//        return transferDao.getTransferSum(id);
//    }

    //TRANSFER STATUS'
    @PreAuthorize("permitAll")
    @GetMapping(path = "transferstatus/")
    public TransferStatus[] getAllTransferStatus(){
        return transferStatusDAO.getAllTransferStatus();
    }

    @PreAuthorize("permitAll")
    @GetMapping(path="transferstatus/{id}")
    public TransferStatus getTansferStatusWithId(@PathVariable Long id){
        return transferStatusDAO.getTransferStatus(id);
    }

    //TRANSFER TYPES
    @PreAuthorize("permitAll")
    @GetMapping(path = "transfertype/")
    public TransferType[] getAllTransferType(){
        return transferTypeDAO.getAllTranferTypes();
    }

    @PreAuthorize("permitAll")
    @GetMapping(path="transfertype/{id}")
    public TransferType getTansferTypeWithId(@PathVariable Long id){
        return transferTypeDAO.getTransfereTypeById(id);
    }

}