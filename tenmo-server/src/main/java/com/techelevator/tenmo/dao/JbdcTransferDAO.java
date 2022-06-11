package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JbdcTransferDAO implements TransferDao{
      @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    //retrieves all transfers SENT and RECEIVED by user_id
    @Override
    public Transfer[] getTransfersByUserId(Long id) {
        List<Transfer> transfers = new ArrayList<>();

        String SQL = "SELECT * FROM transfer t " +
                "WHERE (account_from IN (SELECT account_id FROM account WHERE user_id = ?)) " +
                "OR (account_to IN (SELECT account_id FROM account WHERE user_id = ?));";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id, id);
        while(results.next()){
            Transfer transfer = mapToRowTransfer(results);
            transfers.add(transfer);
        }
        return transfers.toArray(new Transfer[0]);
    }
    @Override
    public Transfer[] getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        String SQL = "SELECT * FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
        while(results.next()){
            Transfer transfer = mapToRowTransfer(results);
            transfers.add(transfer);
        }
        return transfers.toArray(new Transfer[0]);
    }
    private Transfer mapToRowTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getDouble("amount"));
        return transfer;
    }
}