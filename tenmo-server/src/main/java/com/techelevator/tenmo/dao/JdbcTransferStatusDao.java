package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferStatusDao implements TransferStatusDAO {

@Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public List<TransferStatus> getAllTransfer(){
        List <TransferStatus> t = new ArrayList<>();
        String SQL = "SELECT * from transfer_status;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);

            while (results.next()) {
                t.add(mapRowToTransferStatus(results));
            } }
            catch(DataAccessException e)
            {
                System.out.print("Error accessing data");
            }
        return t;

    }
    @Override
    public String getTransferStatus(long id){
        String sql = "SELECT transfer_status_desc  from transfer_status WHERE transfer_status_id =  ?;";
            String results = jdbcTemplate.queryForObject(sql, String.class, id);
          return results;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet results){
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransfer_status_id(results.getLong("transfer_status_id"));
        transferStatus.setTransfer_status_desc(results.getString("transfer_status_desc"));
        return transferStatus;
    }
}
