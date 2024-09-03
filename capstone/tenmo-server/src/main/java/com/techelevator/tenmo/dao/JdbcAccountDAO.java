package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
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
        String query = "SELECT account.account_id,account.user_id,account.balance FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?;";
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
    public Account getAccountByAccountId(int accountId){
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
    public Account getAccountByUserId(int userId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, userId);
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
                "where username != ? ORDER BY account.user_id;";
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
    public void updateAccount(BigDecimal amount, int fromAccount, int toAccount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        String sql1 = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        try{
            int numberOfRows = template.update(sql, amount, fromAccount);
            int numberOfRows1 = template.update(sql1,amount, toAccount);
            if(numberOfRows == 0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
    }

    @Override
    public Account updateFromAccount(Account account) {
        Account updateAccount = null;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        int numberOfRows = template.update(sql, account.getBalance(), account.getAccountId());
        try {
            if (numberOfRows == 1) {
                updateAccount = getAccountByAccountId(account.getAccountId());
            } else {
                throw new DaoException("Transfer not found", new Exception());
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Connection Error");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data Problem");
        }
        return updateAccount;
    }

    @Override
    public Account updateToAccount(Account account) {
        Account updateAccount = null;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        int numberOfRows = template.update(sql, account.getBalance(), account.getAccountId());
        try {
            if (numberOfRows == 1) {
                updateAccount = getAccountByAccountId(account.getAccountId());
            } else {
                throw new DaoException("Transfer not found", new Exception());
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Connection Error");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data Problem");
        }
        return updateAccount;    }

    @Override
    public String getUsernameByAccountId(int accountId){
        String user = "";
        String sql = "select username from tenmo_user " +
                "join account on tenmo_user.user_id = account.user_id " +
                "where account_id = ?;";
        SqlRowSet result = template.queryForRowSet(sql,accountId);
        if(result.next()){
            user = result.getString("username");
        }
        return user;
    }

    private Account mapRowToAccount (SqlRowSet sqlRowSet) {
        Account account = new Account();
        account.setAccountId(sqlRowSet.getInt("account_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        return account;
    }
    private Account map(SqlRowSet sqlRowSet){
        Account account = new Account();
        account.setUsername(sqlRowSet.getString("username"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        return account;
    }
}
