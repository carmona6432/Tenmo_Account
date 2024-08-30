package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUsername;
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

    public List<TransferUsername> getTransfersFromAccount(int accountId) {
        List<TransferUsername> transfers = new ArrayList<>();
        String sql = "select transfer_id, username, amount from transfer " +
                "JOIN account ON transfer.account_to = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "where transfer_type_id = 2 and account_from = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            while (results.next()) {
                transfers.add(mapRow(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }
        return transfers;
    }

    @Override
    public List<TransferUsername> getTransfersToAccount(int accountId) {
        List<TransferUsername> transfers = new ArrayList<>();
        String sql = "select transfer_id, username, amount from transfer " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "where transfer_type_id = 2 and account_to = ?";
        ;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            while (results.next()) {
                transfers.add(mapRow(results));
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
    public List<TransferUsername> getPendingTransfersById(int userId) {
            List<TransferUsername> pendingTransfers = new ArrayList<>();
            String sql = "SELECT transfer_id, username, amount FROM transfer ts " +
                    "JOIN account ac ON ac.account_id = ts.account_to " +
                    "JOIN tenmo_user tu ON tu.user_id = ac.user_id " +
                    "WHERE account_from = ? " +
                    "AND transfer_status_id = 1;";
            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);

                while (results.next()) {
                    TransferUsername transfer = mapRow(results);
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
        public void updateTransfer (Transfer transfer){
            String sql = "UPDATE transfers " +
                    "SET transfer_status_id = ?" +
                    "WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getId());
        }

        @Override
        public void createTransfer (Transfer transfer){
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

        private Transfer mapRowToTransfer (SqlRowSet sqlRowSet){
            Transfer transfer = new Transfer();
            transfer.setId(sqlRowSet.getInt("transfer_id"));
            transfer.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
            transfer.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
            transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
            transfer.setAccountTo(sqlRowSet.getInt("account_to"));
            transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
            return transfer;
        }

        private TransferUsername mapRow (SqlRowSet sqlRowSet){
            TransferUsername transfer = new TransferUsername();
            transfer.setTransferId(sqlRowSet.getInt("transfer_id"));
            transfer.setUsername(sqlRowSet.getString("username"));
            transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
            return transfer;
        }
    }

