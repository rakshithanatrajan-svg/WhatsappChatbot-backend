package com.chatbot.model;

import java.time.LocalDateTime;

public class MessageLog {

    private String logId;
    private String from;
    private String senderName;
    private String incomingMessage;
    private String botReply;
    private String matchedIntent;
    private LocalDateTime processedAt;
    private long processingTimeMs;
    private boolean matched;

    public MessageLog() {}

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getIncomingMessage() { return incomingMessage; }
    public void setIncomingMessage(String incomingMessage) { this.incomingMessage = incomingMessage; }

    public String getBotReply() { return botReply; }
    public void setBotReply(String botReply) { this.botReply = botReply; }

    public String getMatchedIntent() { return matchedIntent; }
    public void setMatchedIntent(String matchedIntent) { this.matchedIntent = matchedIntent; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }

    public boolean isMatched() { return matched; }
    public void setMatched(boolean matched) { this.matched = matched; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final MessageLog obj = new MessageLog();

        public Builder logId(String logId) { obj.logId = logId; return this; }
        public Builder from(String from) { obj.from = from; return this; }
        public Builder senderName(String senderName) { obj.senderName = senderName; return this; }
        public Builder incomingMessage(String incomingMessage) { obj.incomingMessage = incomingMessage; return this; }
        public Builder botReply(String botReply) { obj.botReply = botReply; return this; }
        public Builder matchedIntent(String matchedIntent) { obj.matchedIntent = matchedIntent; return this; }
        public Builder processedAt(LocalDateTime processedAt) { obj.processedAt = processedAt; return this; }
        public Builder processingTimeMs(long processingTimeMs) { obj.processingTimeMs = processingTimeMs; return this; }
        public Builder matched(boolean matched) { obj.matched = matched; return this; }

        public MessageLog build() { return obj; }
    }
}
