package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.xml.transform.TransformerFactory;
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
    public List<Transfer> getTransfersById(int userId) {
        return null;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        return null;
    }

    @Override
    public List<Transfer> getPendingTransfersById(int userId) {
        List<Transfer> pendingTransfers =new ArrayList<>();
        String sql = "SELECT transfer_id, tu.username, amount FROM transfer ts " +
                "JOIN account ac ON ac.account_id = ts.account_to " +
                "JOIN tenmo_user tu ON tu.user_id = ac.user_id " +
                "WHERE ts.account_from = ? " +
                "AND transfer_status_id = 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

            while (results.next()){
                Transfer transfer = mapRowToTransfer
            }
        }
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
}
