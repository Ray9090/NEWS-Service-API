package com.app.NewsService.controller;

import com.app.NewsService.model.Account;
import com.app.NewsService.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllAccounts(@RequestParam String role) {
        if (role.equals("admin")) {
            return accountService.allAccounts();
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAccount(@RequestBody Account account, @RequestParam String role) {
        if (role.equals("admin")) {
            return accountService.createAccount(account);
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateAccount(@RequestBody Account account, @RequestParam String role) {
        if (role.equals("admin")) {
            return accountService.updateAccount(account);
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam Integer accountId, @RequestParam String role) {
        if (role.equals("admin")) {
            return accountService.deleteAccount(accountId);
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
        return accountService.login(username, password);
    }
}
