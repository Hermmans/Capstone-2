package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JbdcTransferTypeDao implements TransferTypeDAO {


    private JdbcTemplate jdbcTemplate;

    public JbdcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferType[] getAllTranferTypes(){
        List <TransferType> t = new ArrayList<>();
        String SQL = "SELECT * FROM transfer_type;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
            while (results.next()) {
                TransferType transferType = mapRowToTransferType(results);
                t.add(transferType);
            }
        } catch(DataAccessException e) {
            System.out.print("Error accessing data");
        }
        return t.toArray(new TransferType[0]);

    }

    @Override
    public TransferType getTransfereTypeById(Long id) {
        TransferType transferType = null;
        String SQL = "SELECT * FROM transfer_type WHERE transfer_type_id =  ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id);
        while(results.next()){
            transferType = mapRowToTransferType(results);
        }
        return transferType;
    }


    private TransferType mapRowToTransferType(SqlRowSet results){
        TransferType transferType = new TransferType();
        transferType.setTransfer_type_id(results.getLong("transfer_type_id"));
        transferType.setTransfer_type_desc(results.getString("transfer_type_desc"));
        return transferType;
    }
}
