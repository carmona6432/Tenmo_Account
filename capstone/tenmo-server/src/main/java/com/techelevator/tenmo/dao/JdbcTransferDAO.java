package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
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
    public List<Transfer> getTransfersFromAccount(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer.transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, " +
                "transfer.account_from, transfer.account_to, transfer.amount from transfer " +
                "JOIN account ON transfer.account_to = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "where account_from = ?;";
        try {
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
    public List<Transfer> getTransfersToAccount(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer.transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, " +
                "transfer.account_from, transfer.account_to,transfer.amount from transfer " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "where account_to = ?";
        ;
        try {
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
        Transfer transfer = null;

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return transfer;
    }
    @Override
    public List<Transfer> getPendingTransfers(int accountFrom) {
            List<Transfer> pendingTransfers = new ArrayList<>();
            String sql = "SELECT * FROM transfer " +
                    "WHERE account_from = ? " +
                    "AND transfer_status_id = 1;";
            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountFrom);

                while (results.next()) {
                    pendingTransfers.add(mapRowToTransfer(results));
                }
            } catch (CannotGetJdbcConnectionException e) {
                System.out.println("Problem connecting");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Data problems");
            }
            return pendingTransfers;
        }

        @Override
        public Transfer updateTransfer (Transfer transfer){
        Transfer updateTransfer = null;
            String sql = "UPDATE transfer " +
                    "SET transfer_status_id = ? " +
                    "WHERE transfer_id = ?;";
            int numOfRows =jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
            try {
                if (numOfRows == 1) {
                    updateTransfer = getTransferByTransferId(transfer.getTransferId());
                } else {
                    throw new DaoException("Transfer not found", new Exception());
                }
            } catch (CannotGetJdbcConnectionException e) {
                System.out.println("Connection Error");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Data Problem");
            }
            return updateTransfer;
        }

    @Override
    public void createTransfer (Transfer transfer) {
        int transferId = 0;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        try {
            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Connection Error");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data Problem");
        }
    }
    @Override
    public int getTransferIdLimitOne() {
        int transferId = 0;
        String query = "select transfer_id from transfer " +
                "order by transfer_id desc limit 1;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(query);
        if (result.next()) {
            transferId = result.getInt("transfer_id");
        }
        return transferId;
    }

    @Override
    public String getTransferStatusById(int transferStatusId) {
        String sql = "SELECT transfer_status_id, transfer_status_desc " +
                "FROM transfer_status WHERE transfer_status_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        String transferStatus = null;
        if (result.next()) {
            transferStatus = result.getString("transfer_status_desc");
        }
        return transferStatus;
    }
    @Override
    public String getTransferTypeById(int transferTypeId) {
        String sql = "SELECT transfer_type_id, transfer_type_desc " +
                "FROM transfer_type WHERE transfer_type_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferTypeId);
        String  transferType = null;
        if (result.next()) {
            transferType = result.getString("transfer_type_desc");
        }
        return transferType;
    }

        private Transfer mapRowToTransfer (SqlRowSet sqlRowSet){
            Transfer transfer = new Transfer();
            transfer.setTransferId(sqlRowSet.getInt("transfer_id"));
            transfer.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
            transfer.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
            transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
            transfer.setAccountTo(sqlRowSet.getInt("account_to"));
            transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
            return transfer;
        }
    }

