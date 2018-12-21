package ru.hitrerros.purse.clientcommons;

public enum ReturnCode {

    OK_SAVE(0),
    NOT_FOUND(1);

    private  int code;

    ReturnCode(int code){
        this.code = code;

    }

}
