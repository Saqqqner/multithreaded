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
    @Value("${multithreaded.size.threads}")
    private int threadSize;
    @Value("${multithreaded.size.operations}")
    private int transactionOperationSize;


    public TransactionService(AccountService accountService) {
        this.accountService = accountService;
        this.random = new Random();
    }

    public void performTransaction() {
        AtomicInteger transactionCount = new AtomicInteger(0);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            executor.scheduleWithFixedDelay(() -> {
                if (transactionCount.get() < transactionOperationSize) {
                    Account from = accountService.getRandomAccount();
                    Account to = accountService.getRandomAccount();
                    if (from != to) {
                        Account firstAccount = from.getId().compareTo(to.getId()) < 0 ? from : to;
                        Account secondAccount = from.getId().compareTo(to.getId()) < 0 ? to : from;
                        synchronized (firstAccount) {
                            synchronized (secondAccount) {
                                int amount = random.nextInt(1000);
                                if (from.getMoney() >= amount) {
                                    from.setMoney(from.getMoney() - amount);
                                    to.setMoney(to.getMoney() + amount);
                                    log.info("Transaction № {} :  {} -> {} , amount {}", transactionCount.incrementAndGet(), from.getId(), to.getId(), amount);
                                } else {
                                    log.warn("Insufficient funds for transaction from {}", from.getId());
                                }
                            }
                        }
                    }
                }
            }, 0, getRandomInterval(), TimeUnit.MILLISECONDS);
        }
    }


    private long getRandomInterval() {
        return random.nextInt(1001) + 1000;
    }
}


