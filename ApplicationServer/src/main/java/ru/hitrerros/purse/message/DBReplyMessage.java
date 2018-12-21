package ru.hitrerros.purse.message;

import ru.hitrerros.purse.clientcommons.Application;

import java.util.List;

public class DBReplyMessage extends Message {

    public DBReplyMessage(){}



    public DBReplyMessage(String ownerId, Application senderType) {
        super(ownerId, senderType);
    }

    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
