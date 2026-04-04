package com.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BotResponse {

    @JsonProperty("to")
    private String to;

    @JsonProperty("reply")
    private String reply;

    @JsonProperty("originalMessage")
    private String originalMessage;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("status")
    private String status;

    @JsonProperty("botName")
    private String botName = "WhatsBot";

    public BotResponse() {}

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public String getOriginalMessage() { return originalMessage; }
    public void setOriginalMessage(String originalMessage) { this.originalMessage = originalMessage; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBotName() { return botName; }
    public void setBotName(String botName) { this.botName = botName; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final BotResponse obj = new BotResponse();

        public Builder to(String to) { obj.to = to; return this; }
        public Builder reply(String reply) { obj.reply = reply; return this; }
        public Builder originalMessage(String originalMessage) { obj.originalMessage = originalMessage; return this; }
        public Builder messageId(String messageId) { obj.messageId = messageId; return this; }
        public Builder timestamp(String timestamp) { obj.timestamp = timestamp; return this; }
        public Builder status(String status) { obj.status = status; return this; }
        public Builder botName(String botName) { obj.botName = botName; return this; }

        public BotResponse build() { return obj; }
    }
}
