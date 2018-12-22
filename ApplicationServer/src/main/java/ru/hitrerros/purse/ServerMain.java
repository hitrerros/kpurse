package ru.hitrerros.purse;

import ru.hitrerros.purse.businesslogic.BusinessWorker;

class ServerMain {

    public static void main(String[] args) throws Exception {
        new BusinessWorker().start();
    }

}
