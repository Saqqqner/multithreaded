package ru.adel.multithreaded.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.adel.multithreaded.domain.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Test
    void performTransaction_CheckTotalMoney() {
        // given
        int initialTotalMoney = getTotalMoneyOnAllAccounts();
        System.out.println(initialTotalMoney);

        // when
        transactionService.performTransaction();

        // then
        int finalTotalMoney = getTotalMoneyOnAllAccounts();
        System.out.println(finalTotalMoney);
        assertEquals(initialTotalMoney, finalTotalMoney);
    }


    private int getTotalMoneyOnAllAccounts() {
        return accountService.getAccounts()
                .stream()
                .mapToInt(Account::getMoney)
                .sum();
    }
}