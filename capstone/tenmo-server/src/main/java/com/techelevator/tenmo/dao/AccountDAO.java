package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    Account getAccount(String username);
    Account getAccountById(int account_id);
    Account getAccountByUserId(int user_id);
    List<Account> getAccounts(String username);
    void updateAccount(Account account, int amount, int from_account, int to_account);




}
