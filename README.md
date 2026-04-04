# 🤖 WhatsApp Chatbot Backend — Spring Boot

A production-grade **WhatsApp chatbot backend simulation** built with **Java 17** and **Spring Boot 3**. Exposes a REST API (`/webhook`) that accepts JSON-simulated WhatsApp messages, processes them through an intent-matching engine, returns smart replies, logs all activity, and tracks real-time statistics.

---

## 🚀 Features

| Feature | Details |
|---|---|
| ✅ Webhook endpoint | `POST /webhook` accepts WhatsApp-style JSON |
| 🧠 Intent matching | 25+ keywords with exact & partial match |
| 📋 Message logging | Every message stored in-memory with metadata |
| 📊 Statistics API | Match rate, top senders, intent breakdown |
| 🛡️ Input validation | Field-level validation with clean error responses |
| 🔍 Request logging | Every HTTP request/response logged |
| 🐳 Docker ready | Multi-stage Dockerfile for production |
| ☁️ Render-ready | Deploy with one click using `PORT` env var |

---

## 📦 Tech Stack

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Web** (REST API)
- **Spring Validation** (Input validation)
- **Spring Actuator** (Health checks)
- **Lombok** (Boilerplate reduction)
- **JUnit 5 + MockMvc** (Unit & Integration Tests)

---

## 🗂️ Project Structure

```
whatsapp-chatbot/
├── src/
│   ├── main/java/com/chatbot/
│   │   ├── WhatsAppChatbotApplication.java   ← Entry point
│   │   ├── controller/
│   │   │   └── WebhookController.java        ← REST endpoints
│   │   ├── service/
│   │   │   └── ChatbotService.java           ← Intent engine + logging
│   │   ├── model/
│   │   │   ├── IncomingMessage.java
│   │   │   ├── BotResponse.java
│   │   │   ├── MessageLog.java
│   │   │   └── BotStats.java
│   │   ├── config/
│   │   │   └── GlobalExceptionHandler.java   ← Error handling
│   │   └── filter/
│   │       └── RequestLoggingFilter.java     ← HTTP logging
│   └── resources/
│       └── application.properties
├── Dockerfile
├── pom.xml
└── README.md
```

---

## ⚡ Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Locally

```bash
# 1. Clone the repo
git clone https://github.com/YOUR_USERNAME/whatsapp-chatbot.git
cd whatsapp-chatbot

# 2. Build
mvn clean package -DskipTests

# 3. Run
java -jar target/whatsapp-chatbot-1.0.0.jar
```

The server starts at **http://localhost:8080** 🎉

---

## 📡 API Reference

### `POST /webhook` — Send a message

```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "from": "+91-9876543210",
    "senderName": "Arjun",
    "message": "hi",
    "messageId": "msg-001"
  }'
```

**Response:**
```json
{
  "to": "+91-9876543210",
  "reply": "Hello! 👋 Welcome! How can I assist you today?",
  "originalMessage": "hi",
  "messageId": "BOT-1712345678",
  "timestamp": "2024-04-05 10:30:00",
  "status": "sent",
  "botName": "WhatsBot 🤖"
}
```

---

### `GET /webhook` — Health check

```bash
curl http://localhost:8080/webhook
```

---

### `GET /api/logs` — All message logs

```bash
curl http://localhost:8080/api/logs
```

---

### `GET /api/logs/recent?limit=5` — Recent logs

```bash
curl "http://localhost:8080/api/logs/recent?limit=5"
```

---

### `GET /api/stats` — Bot statistics

```bash
curl http://localhost:8080/api/stats
```

**Response:**
```json
{
  "totalMessagesReceived": 42,
  "totalRepliesSent": 38,
  "unmatchedMessages": 4,
  "matchRate": 90.5,
  "uptimeSince": "2024-04-05 09:00:00",
  "intentBreakdown": { "hi": 15, "bye": 8, "help": 5 },
  "topSenders": { "+91-9876543210": 10, "+91-1234567890": 7 }
}
```

---

### `GET /api/intents` — All supported keywords

```bash
curl http://localhost:8080/api/intents
```

---

## 🧠 Supported Intents

| Keyword(s) | Bot Reply |
|---|---|
| `hi`, `hello`, `hey` | Greeting response |
| `bye`, `goodbye`, `cya` | Farewell response |
| `help`, `support` | Help menu |
| `hours` | Business hours |
| `location`, `address` | Office location |
| `price`, `cost`, `plan` | Pricing info |
| `thanks`, `thank you` | Appreciation response |
| `how are you` | Bot status |
| Unknown | Default + help suggestion |

---

## 🧪 Run Tests

```bash
mvn test
```

Tests include:
- ✅ Intent matching (hi → Hello, bye → Goodbye)
- ✅ Unknown message fallback
- ✅ Message logging verification
- ✅ Statistics accuracy
- ✅ Case-insensitive matching
- ✅ API endpoint tests (MockMvc)
- ✅ Validation error handling

---

## 🐳 Docker

```bash
# Build image
docker build -t whatsapp-chatbot .

# Run container
docker run -p 8080:8080 whatsapp-chatbot
```

---

## ☁️ Deploy on Render

1. Push to GitHub
2. Go to [render.com](https://render.com) → New Web Service
3. Connect your repo
4. Set **Build Command**: `mvn clean package -DskipTests`
5. Set **Start Command**: `java -jar target/whatsapp-chatbot-1.0.0.jar`
6. Set Environment Variable: `PORT=8080`
7. Click **Deploy** 🚀

Or use the `Dockerfile` for a Docker-based deploy.

---

## 📬 Sample cURL Commands

```bash
# Test greetings
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-111","message":"hi"}'

curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-111","message":"bye"}'

# Test business info
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-111","message":"hours"}'

# Test unknown message
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-111","message":"random text"}'

# Check stats
curl http://localhost:8080/api/stats

# Check health
curl http://localhost:8080/actuator/health
```

---

## 👤 Author

Built with ❤️ using Java 17 + Spring Boot 3 for the WhatsApp Chatbot Simulation assignment.
