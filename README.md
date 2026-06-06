# CyberAudit Pro

Full-stack security audit and vulnerability management platform.

## Features

- **JWT authentication** — register/login with role-based access
- **Real-time vulnerability scanner** — port scan, security headers, SSL checks, path discovery
- **AI-powered audits** — Claude API deep analysis (optional, requires API key)
- **Secure coding labs** — AI code vulnerability analysis
- **Audit trail** — server-side SOC 2 style event logging
- **Vulnerability catalog** — persisted findings from scans and audits

## Prerequisites

- **Java 21** (Eclipse Temurin recommended)
- **Node.js 18+** and npm
- **Anthropic API key** (optional, for AI audit and labs)

## Quick Start (Windows)

### 1. Start the backend

```powershell
cd "Cyberaudit Pro"
.\START_BACKEND.ps1
```

Default login: **admin** / **changeme**

### 2. Start the frontend (new terminal)

```powershell
cd frontend
npm install
npm run dev
```

Open http://localhost:5173

### 3. Optional: enable AI features

```powershell
$env:ANTHROPIC_API_KEY = "your-anthropic-api-key"
```

Restart the backend after setting the key.

## Workflow

1. Sign in at `/login`
2. **Audit Targets** → add a real endpoint you own or have permission to test
3. **Dashboard** → **Run Security Scan** (real network/header scan, no API key needed)
4. **Real-Time Scanner** → scan any URL directly
5. **AI Deep Audit** → requires `ANTHROPIC_API_KEY`
6. **Secure Coding Labs** → paste code for AI analysis (requires API key)
7. **SOC 2 Audit Trail** → view server-generated security events

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `dev` = H2 in-memory, `prod` = PostgreSQL |
| `JWT_SECRET` | (dev default) | JWT signing secret — **change in production** |
| `ADMIN_USERNAME` | `admin` | Initial admin username |
| `ADMIN_PASSWORD` | `changeme` | Initial admin password — **change in production** |
| `ANTHROPIC_API_KEY` | — | Required for AI audit and labs |
| `DATABASE_URL` | — | PostgreSQL URL (prod profile) |
| `DATABASE_USERNAME` | — | PostgreSQL user (prod profile) |
| `DATABASE_PASSWORD` | — | PostgreSQL password (prod profile) |

Copy `.env.example` for a template.

## Production

```powershell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DATABASE_URL = "jdbc:postgresql://host:5432/cyberaudit"
$env:DATABASE_USERNAME = "user"
$env:DATABASE_PASSWORD = "secret"
$env:JWT_SECRET = "long-random-secret-at-least-32-characters"
$env:ADMIN_PASSWORD = "strong-password"
$env:ANTHROPIC_API_KEY = "your-key"
```

Build:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
java "-Dmaven.multiModuleProjectDirectory=$PWD" -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain clean package -DskipTests
java -jar target\cyberaudit-pro-1.0.0.jar
```

## API

Base URL: `http://localhost:8080/api`

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/auth/login` | POST | No | Login |
| `/auth/register` | POST | No | Register |
| `/targets` | GET/POST | Yes | Manage audit targets |
| `/scan/url` | POST | Yes | Scan a URL |
| `/scan/target/{id}` | POST | Yes | Scan registered target |
| `/audit/generate` | POST | Yes | AI security audit |
| `/lab/evaluate` | POST | Yes | AI code analysis |
| `/vulnerabilities` | GET | Yes | List findings |
| `/metrics/system` | GET | Yes | Dashboard metrics |
| `/logs` | GET | Yes | Audit trail |

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.3, Spring Security, JWT, JPA, H2/PostgreSQL
- **Frontend:** React 18, TypeScript, Vite, Tailwind CSS, Axios
