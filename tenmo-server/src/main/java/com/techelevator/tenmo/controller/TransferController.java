package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
    @PreAuthorize("isAuthenticated()")
    public class  TransferController {

        private TransferStatusDAO transferStatusDAO;
        private TransferDao transferDao;
        private TransferTypeDAO transferTypeDAO;

        @Autowired
        public  TransferController( TransferStatusDAO transferStatusDAO, TransferDao transferDao, TransferTypeDAO transferTypeDAO) {
            this.transferStatusDAO = transferStatusDAO;
            this.transferDao = transferDao;
            this.transferTypeDAO = transferTypeDAO;
        }

    //END POINTS that have methods associated with them

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

    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "transfer/{idFrom}/{idTo}/{amount}")
    public Transfer addToTransfer(@Valid @RequestBody Transfer transfer, @PathVariable Long idFrom, @PathVariable Long idTo,@PathVariable Double amount){
        return transferDao.addTransfer(transfer, idFrom, idTo, amount);
    }

    //this I'm currently working on
    @PreAuthorize("permitAll")
    @GetMapping(path = "transfers/{id}/details")
    public Transfer[] listAllTransferDetails(@PathVariable Long id){
        return transferDao.getTransferDetails(id);
    }

      //////////////////////////////////////////////





        //some testing for
    //****************************************
    @PreAuthorize("permitAll")
        @GetMapping(path = "transfer_status")
        public List<TransferStatus> getAllTransferStatus(){
            return transferStatusDAO.getAllTransfer();
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transfer_status/{id}")
    public String getTansferStatusWithId(@PathVariable long id){return transferStatusDAO.getTransferStatus(id);}

    @PreAuthorize("permitAll")
    @GetMapping(path = "transfer_type")
    public List<TransferType> getAllTransferType(){
        return transferTypeDAO.getAllTranferTypes();
    }
    @PreAuthorize("permitAll")
    @GetMapping(path="transfer_type/{id}")
    public String getTansferTypeWithId(@PathVariable long id){return transferTypeDAO.getTransferedType(id);}
//end of irrelavant tests
    //********************************************************************





}