package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDAO {
    public double getBalance(int id);
    public List<Account> getUsers();




}
