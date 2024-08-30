package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    public Account getAccount(String username);
    public Account getAccountById(int account_id);
    public BigDecimal getBalance(String username);

    public List<Account> getAccounts(String username);

    public Account updateAccount(Account account, int amount, int from_account, int to_account);




}
