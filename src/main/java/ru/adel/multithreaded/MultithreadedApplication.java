package ru.adel.multithreaded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.adel.multithreaded.service.TransactionService;

@SpringBootApplication
public class MultithreadedApplication implements CommandLineRunner {
    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication.run(MultithreadedApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        transactionService.performTransaction();
    }
}
