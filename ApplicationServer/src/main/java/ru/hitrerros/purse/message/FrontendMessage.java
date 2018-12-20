package ru.hitrerros.purse.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.hitrerros.purse.businesslogic.FrontendOperation;

public class FrontendMessage extends Message {

 @JsonProperty("operation")
 private FrontendOperation operation;

 public FrontendMessage(){}

 public FrontendOperation getOperation() {
  return operation;
 }

 public void setOperation(FrontendOperation operation) {
  this.operation = operation;
 }
}
