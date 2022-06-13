package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
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


    @PreAuthorize("permitAll")
    @GetMapping(path = "transfers/{id}")
    public Transfer[] listTransferById(@PathVariable Long id) {
        return transferDao.getTransfersByUserId(id);
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "transfers/")
    public Transfer[] listAllTransfers(){
        return transferDao.getAllTransfers();
    }

    //CREATES TRANSFER
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfer/{idFrom}/{idTo}/{amount}")
    public Transfer addToTransfer(@Valid @RequestBody Transfer transfer, @PathVariable Long idFrom, @PathVariable Long idTo,@PathVariable Double amount){
        return transferDao.addTransfer(transfer, idFrom, idTo, amount);
    }

    //this I'm currently working on
    //TRANSFER DETAILS
    @PreAuthorize("permitAll")
    @GetMapping(path = "transfers/{id}/details")
    public Transfer[] listAllTransferDetails(@PathVariable Long id){
        return transferDao.getTransferDetails(id);
    }

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