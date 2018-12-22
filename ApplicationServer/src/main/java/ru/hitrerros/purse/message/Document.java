package ru.hitrerros.purse.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Document {

    @JsonProperty("id")
    private long id;

    @JsonProperty("trans_date")
    private LocalDateTime transDate;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("comment")
    private String comment;

    public Document(){}

    public long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.transDate = localDateTime;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
