package com.techelevator.tenmo.dao;

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
    public BigDecimal getBalance(int id) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, id);

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


    private Account mapRowToAccount (SqlRowSet sqlRowSet) {
        Account account = new Account();
        account.setAccountId(sqlRowSet.getInt("account_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        return account;
    }
}
