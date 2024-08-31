package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    Account getAccount(String username);
    Account getAccountByAccountId(int account_id);
    Account getAccountByUserId(int userId);
    List<Account> getAccounts(String username);
    String getUsernameByAccountId(int account_id);
    void updateAccount(BigDecimal amount, int fromAccount, int toAccount);




}
