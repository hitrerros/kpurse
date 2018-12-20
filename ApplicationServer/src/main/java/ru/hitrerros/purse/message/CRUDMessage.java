package ru.hitrerros.purse.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.hitrerros.purse.businesslogic.CRUDOperation;

public class CRUDMessage extends Message {

    @JsonProperty("operation")
    private CRUDOperation operation;

    public CRUDMessage() {}

    public CRUDOperation getOperation() {
        return operation;
    }

    public void setOperation(CRUDOperation operation) {
        this.operation = operation;
    }
}
