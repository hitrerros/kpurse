package ru.hitrerros.purse.businesslogic;

public enum CRUDOperation {
    CREATE(0),
    READ(1),
    UPDATE(2),
    DELETE(3);

    private int command;

    CRUDOperation(int command) {
        this.command = command;
    }

    public int getCommand(){
        return this.command;
    }

}
