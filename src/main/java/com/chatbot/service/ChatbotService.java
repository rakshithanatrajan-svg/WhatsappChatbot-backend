package com.chatbot.service;

import com.chatbot.model.BotResponse;
import com.chatbot.model.BotStats;
import com.chatbot.model.IncomingMessage;
import com.chatbot.model.MessageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ChatbotService {

    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<MessageLog> messageLogs = new CopyOnWriteArrayList<>();
    private final Map<String, AtomicLong> intentCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> senderCounts = new ConcurrentHashMap<>();
    private final AtomicLong totalMessages = new AtomicLong(0);
    private final AtomicLong unmatchedMessages = new AtomicLong(0);
    private final LocalDateTime startupTime = LocalDateTime.now();

    private static final Map<String, String> INTENT_RESPONSES = new LinkedHashMap<>();

    static {
        INTENT_RESPONSES.put("hi",           "Hello! Welcome! How can I assist you today?");
        INTENT_RESPONSES.put("hello",        "Hey there! Great to hear from you! What can I do for you?");
        INTENT_RESPONSES.put("hey",          "Hey! What's up? How can I help you today?");
        INTENT_RESPONSES.put("good morning", "Good morning! Hope you are having a wonderful day! How can I help?");
        INTENT_RESPONSES.put("good evening", "Good evening! How can I assist you tonight?");
        INTENT_RESPONSES.put("good afternoon","Good afternoon! Hope your day is going great! How can I help?");
        INTENT_RESPONSES.put("bye",          "Goodbye! Have a wonderful day! Feel free to message anytime.");
        INTENT_RESPONSES.put("goodbye",      "See you later! Take care and stay safe!");
        INTENT_RESPONSES.put("see you",      "See you! Have a great time! Come back anytime.");
        INTENT_RESPONSES.put("take care",    "You too! Take care and stay safe!");
        INTENT_RESPONSES.put("cya",          "Cya! Have a blast! Talk soon.");
        INTENT_RESPONSES.put("help",         "Help Menu - I understand: hi/hello/hey, bye/goodbye, help, hours, location, price/cost, thanks/thank you. Just type any of them!");
        INTENT_RESPONSES.put("support",      "I'm here to support you! Type 'help' to see what I can do.");
        INTENT_RESPONSES.put("hours",        "Business Hours: Mon-Fri 9AM-6PM, Sat 10AM-4PM, Sun Closed.");
        INTENT_RESPONSES.put("location",     "Our Location: 123 Tech Street, Innovation Park, Bengaluru, Karnataka 560001.");
        INTENT_RESPONSES.put("contact",      "Contact Us: Phone +91-9876543210, Email support@chatbot.com");
        INTENT_RESPONSES.put("address",      "We are located at 123 Tech Street, Bengaluru. Type 'location' for more details.");
        INTENT_RESPONSES.put("price",        "Our Pricing: Basic Rs.999/month, Pro Rs.2499/month, Enterprise Custom. Contact us for a free demo!");
        INTENT_RESPONSES.put("cost",         "Our plans start from Rs.999/month. Type 'price' for full pricing details!");
        INTENT_RESPONSES.put("plan",         "We have Basic, Pro, and Enterprise plans. Type 'price' to see full details!");
        INTENT_RESPONSES.put("thanks",       "You're welcome! Happy to help! Feel free to ask anytime.");
        INTENT_RESPONSES.put("thank you",    "My pleasure! Is there anything else I can help you with?");
        INTENT_RESPONSES.put("awesome",      "Glad you think so! Let me know if you need anything else.");
        INTENT_RESPONSES.put("great",        "Wonderful! Anything else I can assist you with?");
        INTENT_RESPONSES.put("how are you",  "I'm doing great, thank you for asking! Ready to assist you!");
        INTENT_RESPONSES.put("ok",           "Got it! Let me know if you need anything else.");
        INTENT_RESPONSES.put("okay",         "Alright! I'm here if you need me.");
        INTENT_RESPONSES.put("yes",          "Great! How can I assist you further?");
        INTENT_RESPONSES.put("no",           "Alright! Feel free to reach out if you change your mind.");
    }

    public BotResponse processMessage(IncomingMessage incoming) {
        long startTime = System.currentTimeMillis();
        totalMessages.incrementAndGet();

        String sender = incoming.getFrom();
        String rawText = incoming.getMessage();
        String normalizedText = rawText.trim().toLowerCase();

        log.info("Incoming | From: {} ({}) | Message: \"{}\"",
                sender,
                incoming.getSenderName() != null ? incoming.getSenderName() : "Unknown",
                rawText);

        String matchedIntent = findIntent(normalizedText);
        String reply;
        boolean matched = matchedIntent != null;

        if (matched) {
            reply = INTENT_RESPONSES.get(matchedIntent);
            intentCounts.computeIfAbsent(matchedIntent, k -> new AtomicLong(0)).incrementAndGet();
            log.info("Intent matched: \"{}\" -> Replying: \"{}\"", matchedIntent, reply);
        } else {
            unmatchedMessages.incrementAndGet();
            reply = buildDefaultReply(rawText);
            intentCounts.computeIfAbsent("unmatched", k -> new AtomicLong(0)).incrementAndGet();
            log.warn("No intent matched for: \"{}\" -- Sending default response.", rawText);
        }

        senderCounts.computeIfAbsent(sender, k -> new AtomicLong(0)).incrementAndGet();

        long processingTime = System.currentTimeMillis() - startTime;
        String msgId = "BOT-" + System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(FORMATTER);

        MessageLog logEntry = MessageLog.builder()
                .logId(msgId)
                .from(sender)
                .senderName(incoming.getSenderName())
                .incomingMessage(rawText)
                .botReply(reply)
                .matchedIntent(matched ? matchedIntent : "UNMATCHED")
                .processedAt(LocalDateTime.now())
                .processingTimeMs(processingTime)
                .matched(matched)
                .build();

        messageLogs.add(logEntry);

        log.info("Response sent to {} | Intent: {} | Time: {}ms",
                sender, matched ? matchedIntent : "UNMATCHED", processingTime);

        return BotResponse.builder()
                .to(sender)
                .reply(reply)
                .originalMessage(rawText)
                .messageId(msgId)
                .timestamp(timestamp)
                .status("sent")
                .build();
    }

    private String findIntent(String text) {
        if (INTENT_RESPONSES.containsKey(text)) {
            return text;
        }
        return INTENT_RESPONSES.keySet().stream()
                .filter(text::contains)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }

    private String buildDefaultReply(String original) {
        return "I'm not sure how to respond to \"" + original + "\". "
                + "Type 'help' to see what I understand, or contact support: +91-9876543210";
    }

    public List<MessageLog> getAllLogs() {
        return Collections.unmodifiableList(messageLogs);
    }

    public List<MessageLog> getRecentLogs(int limit) {
        int size = messageLogs.size();
        if (size == 0) return Collections.emptyList();
        int fromIndex = Math.max(0, size - limit);
        return Collections.unmodifiableList(messageLogs.subList(fromIndex, size));
    }

    public BotStats getStats() {
        long total = totalMessages.get();
        long unmatched = unmatchedMessages.get();
        long matched = total - unmatched;

        Map<String, Long> intentBreakdown = new LinkedHashMap<>();
        for (Map.Entry<String, AtomicLong> e : intentCounts.entrySet()) {
            intentBreakdown.put(e.getKey(), e.getValue().get());
        }

        List<Map.Entry<String, AtomicLong>> senderList = new ArrayList<>(senderCounts.entrySet());
        senderList.sort((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()));

        Map<String, Long> topSenders = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(10, senderList.size()); i++) {
            Map.Entry<String, AtomicLong> e = senderList.get(i);
            topSenders.put(e.getKey(), e.getValue().get());
        }

        double matchRate = total == 0 ? 0.0 : Math.round((matched * 100.0 / total) * 10) / 10.0;

        return BotStats.builder()
                .totalMessagesReceived(total)
                .totalRepliesSent(matched)
                .unmatchedMessages(unmatched)
                .intentBreakdown(intentBreakdown)
                .topSenders(topSenders)
                .uptimeSince(startupTime.format(FORMATTER))
                .matchRate(matchRate)
                .build();
    }

    public void clearLogs() {
        messageLogs.clear();
        log.info("Message logs cleared.");
    }

    public Map<String, String> getIntentMap() {
        return Collections.unmodifiableMap(INTENT_RESPONSES);
    }
}
