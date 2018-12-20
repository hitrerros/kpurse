package ru.hitrerros.purse;

import ru.hitrerros.purse.businesslogic.BusinessWorker;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        new BusinessWorker().start();
    }

}
