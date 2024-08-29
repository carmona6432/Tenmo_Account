package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDAO implements TransferDAO {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransfer() {
        return null;
    }

    @Override
    public List<Transfer> getTransfersById(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select * from transfer " +
                "where transfer_type_id = 2 " +
                "and account_from = ? or account_to = ?;";
        try{
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
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
    public Transfer getTransferByTransferId(int transferId) {
        return null;
    }

    @Override
    public List<Transfer> getPendingTransfersById(int userId) {
        return null;
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
