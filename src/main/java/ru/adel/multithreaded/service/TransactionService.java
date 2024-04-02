package ru.adel.multithreaded.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.adel.multithreaded.domain.Account;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class TransactionService {
    private final AccountService accountService;
    private final Random random;
    private final AtomicInteger transactionCount = new AtomicInteger(0);
    @Value("${multithreaded.size.threads}")
    private int threadSize;
    @Value("${multithreaded.size.operations}")
    private int transactionOperationSize;


    public TransactionService(AccountService accountService) {
        this.accountService = accountService;
        this.random = new Random();
    }

    public void performTransaction() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            executor.scheduleWithFixedDelay(() -> {
                if (transactionCount.get() < transactionOperationSize) {
                    performSingleTransaction();
                }
            }, 0, getRandomInterval(), TimeUnit.MILLISECONDS);
        }
        executor.shutdown();
    }

    public void performSingleTransaction() {
        Account from = accountService.getRandomAccount();
        Account to = accountService.getRandomAccount();
        if (from != to) {
            Account firstAccount = from.getId().compareTo(to.getId()) < 0 ? from : to;
            Account secondAccount = from.getId().compareTo(to.getId()) < 0 ? to : from;
            synchronizedAccounts(firstAccount, secondAccount, transactionCount);
        }else{
            log.warn("Cannot perform transaction: 'from' and 'to' accounts are the same.");
        }

    }

    public void synchronizedAccounts(Account firstAccount, Account secondAccount, AtomicInteger transactionCount) {
        synchronized (firstAccount) {
            synchronized (secondAccount) {
                int amount = random.nextInt(1000);
                if (firstAccount.getMoney() >= amount) {
                    firstAccount.setMoney(firstAccount.getMoney() - amount);
                    secondAccount.setMoney(secondAccount.getMoney() + amount);
                    log.info("Transaction № {} :  {} -> {} , amount {}",
                            transactionCount.incrementAndGet(), firstAccount.getId(), secondAccount.getId(), amount);
                } else {
                    log.warn("Insufficient funds for transaction from {}",
                            firstAccount.getId());
                }
            }
        }
    }

    private long getRandomInterval() {
        return random.nextLong(1001) + 1000;
    }
}


