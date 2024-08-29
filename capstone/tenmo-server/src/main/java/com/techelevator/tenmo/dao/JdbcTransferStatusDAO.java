package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcTransferStatusDAO implements TransferStatusDAO{
    private JdbcTemplate jdbcTemplate;
    public JdbcTransferStatusDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public TransferStatus getTransferStatusByDescription(String description) {
        String sql = "SELECT transfer_status_id, transfer_status_desc " +
                "FROM transfer_status WHERE transfer_status_desc = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);
        TransferStatus transferStatus = null;
        if (result.next()) {
            int transferStatusId = result.getInt("transfer_status_id");
            String transferStatusDescription = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusId, transferStatusDescription);
        }
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(int transferStatusId) {
        String sql = "SELECT transfer_status_id, transfer_status_desc " +
                "FROM transfer_status WHERE transfer_status_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        TransferStatus transferStatus = null;
        if (result.next()) {
            int newTransferStatusId = result.getInt("transfer_status_id");
            String transferStatusDescription = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(newTransferStatusId, transferStatusDescription);
        }
        return transferStatus;
    }
}
