package com.chatbot;

import com.chatbot.model.BotResponse;
import com.chatbot.model.BotStats;
import com.chatbot.model.IncomingMessage;
import com.chatbot.model.MessageLog;
import com.chatbot.service.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WhatsAppChatbotApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatbotService chatbotService;

    @BeforeEach
    void setUp() {
        chatbotService.clearLogs();
    }

    @Test
    @DisplayName("Should reply Hello to hi")
    void testHiReply() {
        IncomingMessage msg = IncomingMessage.builder().from("+91-9999999999").message("hi").build();
        BotResponse response = chatbotService.processMessage(msg);
        assertThat(response.getReply()).containsIgnoringCase("Hello");
    }

    @Test
    @DisplayName("Should reply Goodbye to bye")
    void testByeReply() {
        IncomingMessage msg = IncomingMessage.builder().from("+91-9999999999").message("bye").build();
        BotResponse response = chatbotService.processMessage(msg);
        assertThat(response.getReply()).containsIgnoringCase("Goodbye");
    }

    @Test
    @DisplayName("Should match hello intent")
    void testHelloReply() {
        IncomingMessage msg = IncomingMessage.builder().from("+91-1111111111").message("hello").build();
        BotResponse response = chatbotService.processMessage(msg);
        assertThat(response.getReply()).isNotBlank();
        assertThat(response.getStatus()).isEqualTo("sent");
    }

    @Test
    @DisplayName("Should return default reply for unknown message")
    void testUnknownMessage() {
        IncomingMessage msg = IncomingMessage.builder().from("+91-2222222222").message("zxcvqwer12345").build();
        BotResponse response = chatbotService.processMessage(msg);
        assertThat(response.getReply()).contains("help");
    }

    @Test
    @DisplayName("Message should be logged after processing")
    void testMessageLogging() {
        IncomingMessage msg = IncomingMessage.builder()
                .from("+91-3333333333").senderName("Test User").message("hi").build();
        chatbotService.processMessage(msg);

        List<MessageLog> logs = chatbotService.getAllLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getFrom()).isEqualTo("+91-3333333333");
        assertThat(logs.get(0).getIncomingMessage()).isEqualTo("hi");
        assertThat(logs.get(0).isMatched()).isTrue();
    }

    @Test
    @DisplayName("Stats should reflect processed messages")
    void testStats() {
        chatbotService.processMessage(IncomingMessage.builder().from("+91-1").message("hi").build());
        chatbotService.processMessage(IncomingMessage.builder().from("+91-2").message("bye").build());
        chatbotService.processMessage(IncomingMessage.builder().from("+91-3").message("zzz_unknown").build());

        BotStats stats = chatbotService.getStats();
        assertThat(stats.getTotalMessagesReceived()).isEqualTo(3);
        assertThat(stats.getUnmatchedMessages()).isEqualTo(1);
        assertThat(stats.getMatchRate()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should match case-insensitive messages")
    void testCaseInsensitiveMatching() {
        IncomingMessage msg = IncomingMessage.builder().from("+91-4").message("HI").build();
        BotResponse response = chatbotService.processMessage(msg);
        assertThat(response.getReply()).containsIgnoringCase("Hello");
    }

    @Test
    @DisplayName("GET /webhook should return status 200")
    void testWebhookGet() throws Exception {
        mockMvc.perform(get("/webhook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("POST /webhook with valid payload should return 200")
    void testWebhookPost() throws Exception {
        String payload = """
                {
                    "from": "+91-9876543210",
                    "senderName": "Ravi",
                    "message": "hi",
                    "messageId": "test-001"
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.to").value("+91-9876543210"))
                .andExpect(jsonPath("$.reply").isNotEmpty())
                .andExpect(jsonPath("$.status").value("sent"));
    }

    @Test
    @DisplayName("POST /webhook with missing from should return 400")
    void testWebhookMissingFrom() throws Exception {
        String payload = """
                {
                    "message": "hi"
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("GET /api/stats should return statistics")
    void testStatsEndpoint() throws Exception {
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMessagesReceived").exists())
                .andExpect(jsonPath("$.matchRate").exists());
    }

    @Test
    @DisplayName("GET /api/intents should return intent list")
    void testIntentsEndpoint() throws Exception {
        mockMvc.perform(get("/api/intents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIntents").isNumber())
                .andExpect(jsonPath("$.intents").isMap());
    }

    @Test
    @DisplayName("GET /api/logs should return logs after messages processed")
    void testLogsEndpoint() throws Exception {
        String payload = "{\"from\": \"+91-0000000001\", \"message\": \"hello\"}";
        mockMvc.perform(post("/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));

        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.logs").isArray());
    }
}
