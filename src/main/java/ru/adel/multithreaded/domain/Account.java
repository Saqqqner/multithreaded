package ru.adel.multithreaded.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Account {


    private final String id;

    private int money;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.money = 10000;
    }
}
