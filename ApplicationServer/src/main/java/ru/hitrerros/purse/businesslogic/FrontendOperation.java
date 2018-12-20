package ru.hitrerros.purse.businesslogic;

public enum FrontendOperation {
    INCOME(0),
    WITHDRAWAL(1),
    BALANCE(2);

    private int command;

    FrontendOperation(int command) {
        this.command = command;
    }

    public int getCommand(){
        return this.command;
    }

}
