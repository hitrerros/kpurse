package ru.hitrerros.purse.message;

import ru.hitrerros.purse.clientcommons.Application;

public class InitMessage extends Message {

    public InitMessage(){}

    public InitMessage(String ownerId, Application senderType) {
        super(ownerId, senderType);
    }

}
