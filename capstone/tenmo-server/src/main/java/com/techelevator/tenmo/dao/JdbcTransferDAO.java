package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Transfer> getTransfersFromAccount(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, account_to, amount from transfer " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id" +
                "where transfer_type_id = 2 " +
                "and username = ?;";
        try{
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return transfers;
    }
    @Override
    public List<Transfer> getTransfersToAccount(String username){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, account_from, amount from transfer " +
                "JOIN account ON transfer.account_to = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id" +
                "where transfer_type_id = 2 " +
                "and username = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return transfers;
    }

    @Override
    public List<Transfer> getPendingTransfersById(int userId) {
        List<Transfer> pendingTransfers =new ArrayList<>();
        String sql = "SELECT transfer_id, tu.username, amount FROM transfer ts " +
                "JOIN account ac ON ac.account_id = ts.account_to " +
                "JOIN tenmo_user tu ON tu.user_id = ac.user_id " +
                "WHERE username = ? " +
                "AND transfer_status_id = 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

            while (results.next()){
                Transfer transfer = mapRowToTransfer(results);
                pendingTransfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return pendingTransfers;
    }

    @Override
    public void updateTransfer(Transfer transfer) {

    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try {
            jdbcTemplate.update(sql, transfer.getId(), transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                    transfer.getAccountTo(), transfer.getAmount());
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Connection Error");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data Problem");
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet sqlRowSet) {
        Transfer transfer = new Transfer();
        transfer.setId(sqlRowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfer.setAccountTo(sqlRowSet.getInt("account_to"));
        transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
        return transfer;
    }
}
