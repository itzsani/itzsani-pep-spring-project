package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Account> register(Account account) {
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4 || account.getUsername() == null || account.getUsername().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account saved = accountRepository.save(account);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public ResponseEntity<Account> login(Account account) {
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isEmpty() || !existing.get().getPassword().equals(account.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(existing.get(), HttpStatus.OK);
    }
}
