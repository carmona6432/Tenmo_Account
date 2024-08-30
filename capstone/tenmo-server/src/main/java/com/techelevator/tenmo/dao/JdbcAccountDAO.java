package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
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
    public BigDecimal getBalance(String username) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user on account.user_id = tenmo_user.user_id " +
                "WHERE username = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, username);

            if(results.next()) {
                balance = results.getBigDecimal("balance");
            }

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return balance;
    }

    @Override
    public List<String> getAccounts(int id) {
        List<String> accounts = new ArrayList<>();
        String sql = "SELECT username FROM tenmo_user " +
                "WHERE user_id != ?;";
        SqlRowSet results = template.queryForRowSet(sql,id);
        try{
        while (results.next()){
            accounts.add(results.getString("username"));
        }} catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return accounts;

    }

    @Override
    public Account updateAccount(Account account) {
        Account updateAccount = null;
        String sql = "update account set balance = balance - ? where account_id = ?";
        String sql1 = "update account set balance = balance + ? where account_id = ?;";
        try{
            int numberOfRows = template.update(sql,account.getBalance(),account.getAccountId());
            int numberOfRows1 = template.update(sql1,account.getBalance(),account.getAccountId());
            if(numberOfRows1 == 0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return updateAccount;
    }


    private Account mapRowToAccount (SqlRowSet sqlRowSet) {
        Account account = new Account();
        account.setAccountId(sqlRowSet.getInt("account_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        return account;
    }
}
