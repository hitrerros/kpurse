package ru.hitrerros.purse.message;

import ru.hitrerros.purse.clientcommons.Application;

public class DBReplyMessage extends Message {

    public DBReplyMessage(){}

    public DBReplyMessage(String ownerId, Application senderType) {
        super(ownerId, senderType);
    }




}
