package com.chatbot.controller;

import com.chatbot.model.*;
import com.chatbot.service.ChatbotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Webhook Controller — the entry point for all WhatsApp-simulated messages.
 *
 * Endpoints:
 *   POST /webhook       → Receive and process an incoming message
 *   GET  /webhook       → Health check / bot info
 *   GET  /api/logs      → Retrieve all message logs
 *   GET  /api/logs/recent?limit=N → Recent N logs
 *   GET  /api/stats     → Bot statistics
 *   GET  /api/intents   → All supported intents & replies
 *   DELETE /api/logs    → Clear all logs
 */
@RestController
@CrossOrigin(origins = "*")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final ChatbotService chatbotService;

    public WebhookController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /webhook — Core message handler
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping("/webhook")
    public ResponseEntity<BotResponse> receiveMessage(@Valid @RequestBody IncomingMessage incoming) {
        log.info("🔔 Webhook triggered from: {}", incoming.getFrom());
        BotResponse response = chatbotService.processMessage(incoming);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /webhook — Health check
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/webhook")
    public ResponseEntity<Map<String, Object>> webhookStatus() {
        return ResponseEntity.ok(Map.of(
            "status",      "✅ WhatsApp Chatbot Webhook is LIVE",
            "version",     "1.0.0",
            "description", "POST a JSON body to this endpoint to simulate a WhatsApp message.",
            "samplePayload", Map.of(
                "from",        "+91-9876543210",
                "senderName",  "Arjun",
                "message",     "Hi",
                "messageId",   "msg-001"
            )
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/logs — All message logs
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/api/logs")
    public ResponseEntity<Map<String, Object>> getAllLogs() {
        List<MessageLog> logs = chatbotService.getAllLogs();
        return ResponseEntity.ok(Map.of(
            "total", logs.size(),
            "logs",  logs
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/logs/recent?limit=10 — Recent logs
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/api/logs/recent")
    public ResponseEntity<Map<String, Object>> getRecentLogs(
            @RequestParam(defaultValue = "10") int limit) {
        List<MessageLog> logs = chatbotService.getRecentLogs(limit);
        return ResponseEntity.ok(Map.of(
            "count", logs.size(),
            "logs",  logs
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/stats — Bot statistics
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/api/stats")
    public ResponseEntity<BotStats> getStats() {
        return ResponseEntity.ok(chatbotService.getStats());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/intents — All supported intents
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/api/intents")
    public ResponseEntity<Map<String, Object>> getIntents() {
        return ResponseEntity.ok(Map.of(
            "totalIntents", chatbotService.getIntentMap().size(),
            "intents",      chatbotService.getIntentMap()
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/logs — Clear logs
    // ─────────────────────────────────────────────────────────────────────────
    @DeleteMapping("/api/logs")
    public ResponseEntity<Map<String, String>> clearLogs() {
        chatbotService.clearLogs();
        return ResponseEntity.ok(Map.of(
            "status",  "success",
            "message", "All logs cleared successfully."
        ));
    }
}
