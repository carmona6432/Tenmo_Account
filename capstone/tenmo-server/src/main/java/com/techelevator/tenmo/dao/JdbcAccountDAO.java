package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
@Component
public class JdbcAccountDAO implements AccountDAO {

    private JdbcTemplate template;

    public JdbcAccountDAO(DataSource ds) {
        template = new JdbcTemplate(ds);
    }
    @Override
    public double getBalance(int id) {
        double balance = 0.0;
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = template.queryForRowSet(sql, id);

            if(results.next()) {
                balance = results.getDouble("balance");
            }

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return balance;
    }

    @Override
    public List<Account> getUsers() {
        return null;
    }
}
