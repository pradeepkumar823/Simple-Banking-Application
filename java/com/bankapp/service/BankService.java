package com.bankapp.service;

import com.bankapp.model.BankAccount;
import com.bankapp.model.Transaction;
import com.bankapp.repository.BankAccountRepository;
import com.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class  BankService {

    @Autowired private BankAccountRepository accountRepo;
    @Autowired private TransactionRepository transactionRepo;

    public double getBalance(String username) {
        return accountRepo.findByUsername(username).getBalance();
    }

    public void deposit(String username, double amount) {
        BankAccount account = accountRepo.findByUsername(username);
        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        transactionRepo.save(new Transaction(username, "DEPOSIT", amount, LocalDateTime.now()));
    }

    public boolean withdraw(String username, double amount) {
        BankAccount account = accountRepo.findByUsername(username);
        if(account!=null &&account.getBalance()>=amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepo.save(account);

            transactionRepo.save(new Transaction(username, "WITHDRAW", amount, LocalDateTime.now()));
            return true;
        }
        return false;

        }
}

