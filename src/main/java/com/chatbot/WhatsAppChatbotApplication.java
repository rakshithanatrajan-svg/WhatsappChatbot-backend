package com.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WhatsAppChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsAppChatbotApplication.class, args);
        System.out.println("""
                
                ╔══════════════════════════════════════════════╗
                ║   WhatsApp Chatbot Backend — ONLINE ✓        ║
                ║   POST /webhook  → Send messages             ║
                ║   GET  /webhook  → Health check              ║
                ║   GET  /api/logs → Message history           ║
                ║   GET  /api/stats→ Bot statistics            ║
                ╚══════════════════════════════════════════════╝
                """);
    }
}
