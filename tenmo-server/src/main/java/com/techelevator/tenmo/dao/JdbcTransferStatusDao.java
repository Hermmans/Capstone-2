package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferStatusDao implements TransferStatusDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferStatus[] getAllTransferStatus(){
        List <TransferStatus> t = new ArrayList<>();
        String SQL = "SELECT * from transfer_status;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
            while (results.next()) {
                TransferStatus transferStatus = mapRowToTransferStatus(results);
                t.add(transferStatus);
            }
        } catch(DataAccessException e) {
                System.out.print("Error accessing data");
            }
        return t.toArray(new TransferStatus[0]);

    }

    @Override
    public TransferStatus getTransferStatus(Long id){
        TransferStatus transferStatus = null;
        String SQL = "SELECT * FROM transfer_status WHERE transfer_status_id =  ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id);
        while(results.next()){
            transferStatus = mapRowToTransferStatus(results);
        }
            return transferStatus;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet results){
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransfer_status_id(results.getLong("transfer_status_id"));
        transferStatus.setTransfer_status_desc(results.getString("transfer_status_desc"));
        return transferStatus;
    }
}
