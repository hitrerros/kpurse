package ru.hitrerros.purse.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.hitrerros.purse.businesslogic.CRUDOperation;
import ru.hitrerros.purse.clientcommons.ReturnCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CRUDMessage extends Message {

    @JsonProperty("operation")
    private CRUDOperation operation;

    @JsonProperty("document")
    private Document document;

    public CRUDMessage() {}

    public CRUDOperation getOperation() {
        return operation;
    }

    public void setOperation(CRUDOperation operation) {
        this.operation = operation;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

  }
