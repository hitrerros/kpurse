package ru.hitrerros.purse.clientcommons;

public enum Application {
    FRONTEND(0),
    APPLICATION(1),
    DBSERVER(2);

    private int type;

    Application(int type){
        this.type = type;
    }


}
