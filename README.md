# InterviewAI

InterviewAI is a full-stack AI-powered mock interview platform that helps users prepare for job interviews through realistic AI-driven conversations, automated performance evaluation, and personalized feedback.

The platform simulates role-based interviews using Gemma 3 running locally through Ollama and provides detailed readiness assessments to help candidates improve their interview skills.

---

## Features

### Authentication & Security
- User Registration
- User Login
- JWT Authentication
- Protected Routes
- Password Encryption

### AI Mock Interviews
- Role-Based Interview Generation
- AI Interview Conversations
- Multi-Turn Interview Sessions
- Context-Aware Questioning
- Interview History Tracking

### AI Evaluation
- Readiness Score
- Skill Breakdown Analysis
- Strengths Identification
- Areas for Improvement
- AI Assessment Summary

### Dashboard
- Interview Session Management
- Performance Tracking
- Previous Interview Records
- User Statistics

### Subscription System
- Free Plan
- Premium Plans
- Billing History
- Subscription Management

---

## Tech Stack

### Frontend
- React
- TypeScript
- Tailwind CSS
- React Router
- Axios

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- REST APIs
- WebSockets

### Database
- MySQL

### AI
- Ollama
- Gemma 3

### Tools
- Maven
- Git
- GitHub
- Postman

---

## System Architecture

User
↓
React Frontend
↓
Spring Boot REST APIs
↓
JWT Security Layer
↓
Business Services
↓
MySQL Database

AI Requests
↓
Spring Boot
↓
Ollama
↓
Gemma 3

---

## Screenshots

### Login Page
Secure authentication system with JWT-based authorization.

### Dashboard
Manage interview sessions, track progress, and review performance.

### AI Interview Session
Interactive AI-powered mock interview experience.

### Evaluation Report
Detailed readiness score and personalized feedback.

### Subscription Management
Manage plans and billing information.

---

## Installation

### Clone Repository

```bash
git clone https://github.com/yourusername/interviewai-ai-interview-simulator.git
```

### Backend Setup

```bash
cd backend
```

Configure application properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/interviewai
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secret=your-secret-key

ollama.base-url=http://localhost:11434
```

Run Spring Boot:

```bash
mvn spring-boot:run
```

---

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

---

### Run Ollama

Install Ollama and pull Gemma 3:

```bash
ollama pull gemma3
```

Run:

```bash
ollama run gemma3
```

---

## Future Improvements

- Voice-Based Interviews
- Real-Time AI Streaming Responses
- PDF Interview Reports
- Resume-Based Interviews
- AI Career Recommendations
- Interview Analytics Dashboard
- Multi-Language Support

---

## Project Highlights

- Full-Stack SaaS Architecture
- AI-Powered Interview Simulation
- Automated Candidate Evaluation
- Secure JWT Authentication
- Role-Based Interview Generation
- Gemma 3 Integration via Ollama
- Modern Responsive UI

---

## Author

Farhan Hassin

Computer Science & Engineering Student

Passionate about Software Engineering, Artificial Intelligence, and Data Science.

---
