package ru.hitrerros.purse.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.hitrerros.purse.clientcommons.Application;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CRUDMessage.class, name = "CRUDMessage"),
        @JsonSubTypes.Type(value = FrontendMessage.class, name = "FrontendMessage"),
        @JsonSubTypes.Type(value = DBReplyMessage.class, name = "DBReply"),
        @JsonSubTypes.Type(value = InitMessage.class, name = "InitMessage")

})
public abstract class Message {

    @JsonProperty("from")
    private String from;

    @JsonProperty("sender_type")
    private Application senderType;

    public Message( String from, Application senderType) {
        this.from = from;
        this.senderType = senderType;
    }

    public Message() {
    }

    public String getOwnerId() {
        return from;
    }

    public Application getSenderType() {
        return senderType;
    }

    public void setOwnerId(String ownerId) {
        this.from = ownerId;
    }

    public void setSenderType(Application senderType) {
        this.senderType = senderType;
    }
}
