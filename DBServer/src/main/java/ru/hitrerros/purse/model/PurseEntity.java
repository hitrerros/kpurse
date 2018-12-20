package ru.hitrerros.purse.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "record")
public class PurseEntity extends MainEntity {

    @Column(name = "tr_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime transDate;

    @Column(name = "tr_amount")
    private BigDecimal amount;

    @Column(name = "comment")
    private String comment;

    public PurseEntity() {  }

    public PurseEntity(long id,  LocalDateTime transDate, BigDecimal amount, String comment){
        this.id = id;
        this.transDate = transDate;
        this.amount    = amount;
        this.comment   = comment;
    }

    public PurseEntity(LocalDateTime transDate, BigDecimal amount, String comment){
        this.transDate = transDate;
        this.amount    = amount;
        this.comment   = comment;
    }


    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTransDate() {
        return transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }
}
