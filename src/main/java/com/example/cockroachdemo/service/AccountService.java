package com.example.cockroachdemo.service;

import java.util.Optional;

import com.example.cockroachdemo.model.Account;

public interface AccountService {
    void createAccountsTable();
    Optional<Account> getAccount(int id);
    int bulkInsertRandomAccountData(int numberToInsert);
    int addAccounts(Account...accounts);
    int transferFunds(int fromAccount, int toAccount, int amount);
    long findCountOfAccounts();
    int deleteAllAccounts();
}
