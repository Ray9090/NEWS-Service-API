package com.app.NewsService.service;

import com.app.NewsService.model.Account;
import com.app.NewsService.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Object> createAccount(Account account) {
        accountRepository.save(account);
        return new ResponseEntity<>("Account is successfully created", HttpStatus.OK);
    }

    public ResponseEntity<Object> allAccounts() {
        List<Account> accountList = accountRepository.findAll();
        if (accountList.isEmpty()) {
            return new ResponseEntity<>("There is no account in the database", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> deleteAccount(Integer accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            accountRepository.delete(account.get());
            return new ResponseEntity<>("Account is successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no account against this ID", HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> updateAccount(Account updatedAccount) {
        accountRepository.save(updatedAccount);
        return new ResponseEntity<>("Account is successfully updated", HttpStatus.OK);
    }

    public ResponseEntity<Object> login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isPresent()) {
            return new ResponseEntity<>("You are successfully logged In", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad Credentials", HttpStatus.OK);
        }
    }
}
