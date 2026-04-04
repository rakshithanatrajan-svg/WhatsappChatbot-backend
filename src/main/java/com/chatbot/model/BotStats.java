package com.chatbot.model;

import java.util.Map;

public class BotStats {

    private long totalMessagesReceived;
    private long totalRepliesSent;
    private long unmatchedMessages;
    private Map<String, Long> intentBreakdown;
    private Map<String, Long> topSenders;
    private String uptimeSince;
    private double matchRate;

    public BotStats() {}

    public long getTotalMessagesReceived() { return totalMessagesReceived; }
    public void setTotalMessagesReceived(long v) { this.totalMessagesReceived = v; }

    public long getTotalRepliesSent() { return totalRepliesSent; }
    public void setTotalRepliesSent(long v) { this.totalRepliesSent = v; }

    public long getUnmatchedMessages() { return unmatchedMessages; }
    public void setUnmatchedMessages(long v) { this.unmatchedMessages = v; }

    public Map<String, Long> getIntentBreakdown() { return intentBreakdown; }
    public void setIntentBreakdown(Map<String, Long> v) { this.intentBreakdown = v; }

    public Map<String, Long> getTopSenders() { return topSenders; }
    public void setTopSenders(Map<String, Long> v) { this.topSenders = v; }

    public String getUptimeSince() { return uptimeSince; }
    public void setUptimeSince(String v) { this.uptimeSince = v; }

    public double getMatchRate() { return matchRate; }
    public void setMatchRate(double v) { this.matchRate = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final BotStats obj = new BotStats();

        public Builder totalMessagesReceived(long v) { obj.totalMessagesReceived = v; return this; }
        public Builder totalRepliesSent(long v) { obj.totalRepliesSent = v; return this; }
        public Builder unmatchedMessages(long v) { obj.unmatchedMessages = v; return this; }
        public Builder intentBreakdown(Map<String, Long> v) { obj.intentBreakdown = v; return this; }
        public Builder topSenders(Map<String, Long> v) { obj.topSenders = v; return this; }
        public Builder uptimeSince(String v) { obj.uptimeSince = v; return this; }
        public Builder matchRate(double v) { obj.matchRate = v; return this; }

        public BotStats build() { return obj; }
    }
}
