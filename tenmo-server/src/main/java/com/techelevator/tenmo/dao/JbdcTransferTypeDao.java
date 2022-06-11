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

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

        @Override
    public List<TransferType> getAllTranferTypes(){
            List <TransferType> t = new ArrayList<>();
            String SQL = "SELECT * from transfer_type;";

            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
                while (results.next()) {
                    t.add(mapRowToTransferType(results));
                } }
            catch(DataAccessException e)
            {
                System.out.print("Error accessing data");
            }
            return t;
    }
    @Override
    public String getTransferedType(long id){
        String sql = "SELECT transfer_type_desc  from transfer_type WHERE transfer_type_id =  ?;";
        String results = jdbcTemplate.queryForObject(sql, String.class, id);

        return results;    }

    private TransferType mapRowToTransferType(SqlRowSet results){
        TransferType transferType = new TransferType();
        transferType.setTransfer_type_id(results.getLong("transfer_type_id"));
        transferType.setTransfer_type_desc(results.getString("transfer_type_desc"));
        return transferType;
    }
}
