package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    Account getAccount(String username);
    Account getAccountByAccountId(int account_id);
    int getAccountByUserId(int user_id);
    List<Account> getAccounts(String username);
    String getUsernameByAccountId(int account_id);
    void updateAccount(Account account, int amount, int from_account, int to_account);




}
