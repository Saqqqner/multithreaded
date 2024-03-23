package ru.adel.multithreaded.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.adel.multithreaded.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Getter
public class AccountService {
    private final Random random;
    private final List<Account> accounts;
    @Value("${multithreaded.size.accounts}")
    private int accountSize;

    public AccountService() {
        this.accounts = new ArrayList<>();
        this.random = new Random();
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < accountSize; i++) {
            Account account = new Account();
            accounts.add(account);
        }
    }

    public Account getRandomAccount() {
        return accounts.get(random.nextInt(accounts.size()));

    }
}
