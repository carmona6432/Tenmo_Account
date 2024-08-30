package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.sql.RowSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcAccountDAO implements AccountDAO {

    private JdbcTemplate template;

    public JdbcAccountDAO(DataSource ds) {
        template = new JdbcTemplate(ds);
    }

    @Override
    public Account getAccount(String username) {
        Account account = null;
        String query = "select account.account_id,account.user_id,account.balance from account " +
                "join tenmo_user on account.user_id = tenmo_user.user_id " +
                "where username = ?;";
        try{
            SqlRowSet results = template.queryForRowSet(query,username);
            if(results.next()){
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
        System.out.println("Problem connecting");
    } catch (DataIntegrityViolationException e) {
        System.out.println("Data problems");
    }
        return account;
    }
    @Override
    public Account getAccountById(int accountId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, accountId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return account;
    }
    @Override
    public Account getAccountByUserId(int user_id){
        Account account = null;
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, user_id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return account;
    }
    @Override
    public List<Account> getAccounts(String username) {
        List<Account> accounts = new ArrayList<>();
        String sql = "select account.user_id, tenmo_user.username from account " +
                "join tenmo_user on account.user_id = tenmo_user.user_id " +
                "where username != ?;";
        SqlRowSet results = template.queryForRowSet(sql,username);
        try{
        while (results.next()){
            accounts.add(map(results));
        }} catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return accounts;
    }

    @Override
    public void updateAccount(Account account, int amount, int from_account, int to_account) {
        String sql = "update account set balance = balance - ? where account_id = ?";
        String sql1 = "update account set balance = balance + ? where account_id = ?;";
        try{
            int numberOfRows = template.update(sql,account.getBalance(), amount, from_account);
            int numberOfRows1 = template.update(sql1,account.getBalance(),amount, to_account);
            if(numberOfRows == 0){
                throw new DaoException("Zero rows affected, expected at least one");
            } else{
                getAccountById(numberOfRows);
                getAccountById(numberOfRows1);

            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
    }


    private Account mapRowToAccount (SqlRowSet sqlRowSet) {
        Account account = new Account();
        account.setAccountId(sqlRowSet.getInt("account_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        account.setUsername(sqlRowSet.getString("username"));
        return account;
    }
    private Account map(SqlRowSet sqlRowSet){
        Account account = new Account();
        account.setUsername(sqlRowSet.getString("username"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        return account;
    }
}
