package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
    public class JdbcAccountDAO  implements AccountDAO{

        private JdbcTemplate jdbcTemplate;

        public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public Account getBalance(Long userId) {
            Account account = null;
            String SQL = "SELECT balance, user_id, account_id FROM account" +
                    " WHERE user_id = ?;";
            SqlRowSet results = null;

            try {
                results = jdbcTemplate.queryForRowSet(SQL, userId);
                if (results.next()) {
                    account = mapRowToAccount(results);
                }
            } catch (DataAccessException e) {
                System.out.println("Error accessing data");
            }
            return account;
        }

        @Override
        public Account findByUserId(Long id) {
            Account account = null;
            String SQL = "SELECT * FROM account WHERE user_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id);
            while(results.next()){
                account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account findByAccountId(Long id) {
        Account account = null;
        String SQL = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, id);
        while(results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
        public Account[] findAllAccounts() {
            List<Account> accounts = new ArrayList<>();
            String SQL = "SELECT * FROM account;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL);
            while(results.next()){
                Account account = mapRowToAccount(results);
                accounts.add(account);
            }
            return accounts.toArray(new Account[0]);
        }

    @Override
    public Account withdrawAcct(Account account, Long id, Double amount) {
            Account acct = findByUserId(id);
            acct.setBalance(acct.getBalance()-amount);
            String SQL = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(SQL,amount, id);
            return acct;
    }

    @Override
    public Account depositAcct(Account account, Long id, Double amount) {
            Account acct = findByUserId(id);
            acct.setBalance(acct.getBalance()+amount);
            String SQL = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(SQL,amount, id);
            return acct;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getDouble("balance"));
        account.setAccountId(result.getLong("account_id"));
        account.setUserId(result.getLong("user_id"));
        return account;
    }
}

