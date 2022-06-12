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

    //this I'm working on
    //might need an.. Object mapToRow();
    @Override
    public Transfer[] getTransferDetails(Long id){
        List<Transfer> transfers = new ArrayList<>();

        String SQL = "SELECT t.transfer_id, t.account_from, t.account_to, " +
                "tt.transfer_type_desc, ts.transfer_status_desc, t.amount FROM transfer t " +
                "JOIN account a ON a.account_id=t.account_from " +
                "JOIN tenmo_user tu ON tu.user_id=a.user_id " +
                "JOIN transfer_type tt ON tt.transfer_type_id=t.transfer_type_id " +
                "JOIN transfer_status ts ON ts.transfer_status_id=t.transfer_status_id " +
                "WHERE (account_from IN (SELECT account_id FROM account WHERE user_id = ?)) " +
                "OR (account_to IN (SELECT account_id FROM account WHERE user_id = ?));";

        // we need a unique maptorow for all of the details

        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id, id);
        while(results.next()){
            Transfer transfer = mapToRowTransfer(results);
            transfers.add(transfer);
        }
        return transfers.toArray(new Transfer[0]);

    }

    //this isn't generic yet
    //will use for transfer status/type
    @Override
    public Transfer addTransfer(Transfer transfer, Long idFrom, Long idTo, Double amount) {
        String SQL = "INSERT INTO transfer" +
                " (transfer_type_id, " +
                "transfer_status_id, " +
                "account_from, " +
                "account_to, " +
                "amount) " +
                "VALUES (2, 2, ?, ?, ?);";
        jdbcTemplate.update(SQL, idFrom, idTo, amount);
        return null;
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