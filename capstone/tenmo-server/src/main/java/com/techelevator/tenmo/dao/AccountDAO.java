package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    public BigDecimal getBalance(String username);

    public List<String> getAccounts(int id);

    public Account updateAccount(Account account);




}
