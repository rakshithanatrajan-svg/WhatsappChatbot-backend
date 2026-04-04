package com.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class IncomingMessage {

    @NotBlank(message = "Sender phone number is required")
    @JsonProperty("from")
    private String from;

    @NotBlank(message = "Message text cannot be blank")
    @JsonProperty("message")
    private String message;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("senderName")
    private String senderName;

    @JsonProperty("messageType")
    private String messageType = "text";

    public IncomingMessage() {}

    public IncomingMessage(String from, String message, String messageId,
                           String timestamp, String senderName, String messageType) {
        this.from = from;
        this.message = message;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.messageType = messageType != null ? messageType : "text";
    }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String from, message, messageId, timestamp, senderName, messageType;

        public Builder from(String from) { this.from = from; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder messageId(String messageId) { this.messageId = messageId; return this; }
        public Builder timestamp(String timestamp) { this.timestamp = timestamp; return this; }
        public Builder senderName(String senderName) { this.senderName = senderName; return this; }
        public Builder messageType(String messageType) { this.messageType = messageType; return this; }

        public IncomingMessage build() {
            return new IncomingMessage(from, message, messageId, timestamp, senderName, messageType);
        }
    }
}
