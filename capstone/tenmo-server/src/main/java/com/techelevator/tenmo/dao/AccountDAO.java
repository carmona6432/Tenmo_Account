package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    public BigDecimal getBalance(int id);

    List<String> getAccounts(int id);




}
