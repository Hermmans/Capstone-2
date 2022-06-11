package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDAO;
import com.techelevator.tenmo.dao.TransferTypeDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    }
