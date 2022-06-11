package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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

 //addded get balanceObject to make things easier.
 public long getBalanceObject(long id){
     String SQL = "SELECT balance FROM account" +
             " WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(SQL, Long.class,id);
        }
//Hermmans Edited this one brad
        @Override
        public long addToBalance(Long amountToAdd, Long accountId) {
            if( amountToAdd < 0){
                System.out.println("Bruh you ain't slick.");
                return 0;
            }
            long newAmount = getBalanceObject(accountId) + amountToAdd;
            String SQL = "UPDATE account SET balance = ?" +
                    "WHERE user_id = ?";

            int results = jdbcTemplate.update(SQL,newAmount, accountId);
            if(results == 1){
                return newAmount;
            }
            else {
                return 0;
            }


        }

        @Override
        public void updateAccount(Account account) {
            String SQL = "UPDATE account SET balance = ? WHERE account_id = ?;";
            jdbcTemplate.update(SQL, account.getBalance(), account.getAccountId());
        }

        //udpated withdraw
        @Override
        public long withdraw(Long amountToSub, Long accountId) {
          long newAmount = getBalanceObject(accountId) - amountToSub;
          if(newAmount < 0  || amountToSub < 0){
              System.out.println("Not enough funds");
              return 0;
          }
            String SQL = "UPDATE account SET balance = ?" +
                    "WHERE user_id = ?";
            int results = jdbcTemplate.update(SQL,newAmount, accountId);
                return newAmount;
        }

        @Override
        public Account findByAccountId(Long accountId) {
            return null;
        }

        private Account mapRowToAccount(SqlRowSet result) {
            Account account = new Account();
            account.setBalance(result.getDouble("balance"));
            account.setAccountId(result.getLong("account_id"));
            account.setUserId(result.getLong("user_id"));
            return account;
        }
    }

