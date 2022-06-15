package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JbdcTransferDAO implements TransferDao{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public JbdcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer[] getTransfersByUserId(Long id) {
        List<Transfer> transfers = new ArrayList<>();

        String SQL = "SELECT * FROM transfer t " +
                "JOIN transfer_type tt ON tt.transfer_type_id=t.transfer_type_id " +
                "JOIN transfer_status ts ON ts.transfer_status_id=t.transfer_status_id " +
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
    public TransferDetail[] getTransferDetails(Long id){
        List<TransferDetail> transfers = new ArrayList<>();

        String SQL = "SELECT  t.transfer_id, tu.username as account_from , tu2.username as account_to, " +
                "tt.transfer_type_desc as transfer_type, ts.transfer_status_desc as transfer_status, t.amount as amount FROM transfer t " +
                "INNER JOIN account ac ON account_from =ac.account_id " +
                "INNER JOIN account ac2 on account_to = ac2.account_id " +
                "INNER JOIN tenmo_user tu ON tu.user_id = ac.user_id " +
                "INNER JOIN tenmo_user tu2 ON tu2.user_id = ac2.user_id " +
                "INNER JOIN transfer_type tt ON tt.transfer_type_id = t.transfer_type_id " +
                "INNER JOIN transfer_status ts on ts.transfer_status_id = t.transfer_status_id " +
                "WHERE (account_from IN (SELECT account_id FROM account WHERE user_id = ?)) " +
                "OR (account_to IN (SELECT account_id FROM account WHERE user_id = ?));";

        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id, id);
        while(results.next()){
            TransferDetail transfer = mapToRowTransferDetails(results);
            transfers.add(transfer);
        }
        return transfers.toArray(new TransferDetail[0]);
    }

//    @Override
//    public TransferDetail getTransferSum(Long id){
//        TransferDetail transfers = new TransferDetail();
//
//        String SQL = "SELECT SUM(amount) FROM transfer  " +
//                "WHERE (account_from IN (SELECT account_id FROM account WHERE user_id = ?)) " +
//                "OR (account_to IN (SELECT account_id FROM account WHERE user_id = ?));";
//
//        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id, id);
//        while(results.next()){
//            TransferDetail transfer = mapToRowTransferDetails(results);
//
//        }
//        return transfers;
//    }

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
    public Transfer addTransferFull(Transfer transfer, Long statusId, Long statusTypeId, Long idFrom, Long idTo, Double amount) {
        String SQL = "INSERT INTO transfer" +
                " (transfer_type_id, " +
                "transfer_status_id, " +
                "account_from, " +
                "account_to, " +
                "amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(SQL, statusTypeId, statusId, idFrom, idTo, amount);
        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer, Long typeId, Long statusId, Long transferId) {
        Transfer transfer1 = getTransferByTransferId(transferId);
        transfer1.setTransferId(transfer.getTransferId());
        transfer1.setAccountFrom(transfer.getAccountFrom());
        transfer1.setAccountTo(transfer.getAccountTo());
        transfer1.setTransferStatusDesc(transfer.getTransferStatusDesc());
        transfer1.setTransferTypeDesc(transfer.getTransferTypeDesc());
        transfer1.setAmount(transfer.getAmount());
        transfer1.setTransferTypeId(typeId);
        transfer1.setTransferStatusId(statusId);
        String SQL = "UPDATE transfer t SET transfer_type_id = ?, transfer_status_id= ?  WHERE transfer_id = ?;";
        jdbcTemplate.update(SQL, typeId, statusId, transferId);
    }

    @Override
    public Transfer getTransferByTransferId(Long id) {
        Transfer transfer = new Transfer();
            String SQL ="SELECT * FROM transfer t " +
                    "JOIN transfer_type tt ON tt.transfer_type_id=t.transfer_type_id " +
                    "JOIN transfer_status ts ON ts.transfer_status_id=t.transfer_status_id " +
                    "WHERE t.transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id);
            while(results.next()){
                transfer = mapToRowTransfer(results);
            }
            return transfer;
    }

    @Override
    public Transfer[] getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        String SQL = "SELECT * FROM transfer t " +
                "JOIN transfer_type tt ON tt.transfer_type_id=t.transfer_type_id " +
                "JOIN transfer_status ts ON ts.transfer_status_id=t.transfer_status_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
        while(results.next()){
            Transfer transfer = mapToRowTransfer(results);
            transfers.add(transfer);
        }
        return transfers.toArray(new Transfer[0]);
    }

    private TransferDetail mapToRowTransferDetails(SqlRowSet results){
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setTransferId(results.getLong("transfer_id"));
        transferDetail.setUsernameFrom(results.getString("account_from"));
        transferDetail.setUsernameTo(results.getString("account_to"));
        transferDetail.setTransferTypeDesc(results.getString("transfer_type"));
        transferDetail.setTransferStatusDesc(results.getString("transfer_status"));
        transferDetail.setAmount(results.getDouble("amount"));
        return transferDetail;
    }

    private Transfer mapToRowTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferTypeDesc(results.getString("transfer_type_desc"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setTransferStatusDesc(results.getString("transfer_status_desc"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getDouble("amount"));
        return transfer;
    }

    private Transfer mapToRowTrans(SqlRowSet results){
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