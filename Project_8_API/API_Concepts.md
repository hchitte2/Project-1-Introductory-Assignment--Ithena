# Project Eight: API

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Explain REST and RESTful | 0.5 hr | — | |
| 2 | Explain the architectural style for web APIs | 0.5 hr | Task 1 | |
| 3 | List the HTTP methods REST uses | 0.5 hr | Task 1 | |
| 4 | Compare SOAP and REST | 0.5 hr | Task 1 | |
| 5 | Sign up for an OpenWeatherMap API key (activation can take ~2 hrs) | 0.25 hr | — | |
| 6 | Build the backend that calls OpenWeatherMap | 1.5 hrs | Task 5 | |
| 7 | Build the web page that shows the result | 1 hr | Task 6 | |
| 8 | Tests, README, and review | 1 hr | Tasks 6, 7 | |
| | **Total** | **5.75 hrs** | | |

Start Task 5 first so the key is active by the time the backend is ready.

---

## 1. What are REST and RESTful?

**REST (Representational State Transfer)** is an architectural style for networked applications, defined by Roy Fielding in 2000. It works like this:
- Data is exposed as **resources**, each identified by a **URL** (e.g., `/users/42`).
- Clients use **standard HTTP methods** to read or change those resources.
- The server returns a **representation** of the resource, usually JSON.

**RESTful** describes an API that actually follows REST's rules. REST is the style; a RESTful API is an implementation of it.

## 2. The Architectural Style for Web APIs

REST is defined by six constraints:

| Constraint | Meaning |
|------------|---------|
| Client–server | The user interface and the data storage are separate, so each can change independently |
| Stateless | Each request carries everything the server needs (e.g., an auth token). The server stores no client session |
| Cacheable | Responses state whether they can be cached, which reduces load and latency |
| Uniform interface | Resources have URLs, are changed through standard methods, and messages describe themselves |
| Layered system | The client cannot tell whether it is talking to the server directly or through proxies, load balancers, or gateways |
| Code on demand (optional) | The server may send code to run on the client (e.g., JavaScript) |

**Common design conventions:**
- Use nouns in URLs, not verbs: `/orders`, not `/getOrders`.
- Use plural collection names, and nest related resources: `/customers/7/orders`.
- Return meaningful status codes: 200 OK, 201 Created, 400 Bad Request, 404 Not Found, 500 Server Error.
- Use JSON bodies.
- Version the API: `/v1/...`.
- Filter and paginate with query parameters: `?city=London&page=2`.

## 3. HTTP Methods Supported by REST

| Method | Purpose | CRUD | Idempotent* | Example |
|--------|---------|------|-------------|---------|
| GET | Read a resource | Read | Yes (also "safe": changes nothing) | `GET /users/42` |
| POST | Create a resource | Create | No | `POST /users` |
| PUT | Replace a resource completely | Update | Yes | `PUT /users/42` |
| PATCH | Update part of a resource | Update | Not guaranteed | `PATCH /users/42` |
| DELETE | Remove a resource | Delete | Yes | `DELETE /users/42` |
| HEAD | Like GET, but returns headers only | — | Yes | `HEAD /users/42` |
| OPTIONS | List the allowed methods (used for CORS) | — | Yes | `OPTIONS /users` |

\*Idempotent: sending the same request several times has the same effect as sending it once.

## 4. SOAP vs. REST

| | SOAP | REST |
|--|------|------|
| What it is | A **protocol** (W3C standard) | An **architectural style** |
| Message format | XML only, wrapped in a SOAP envelope | Any format; usually JSON (also XML, HTML, text) |
| Transport | HTTP, SMTP, TCP, JMS | HTTP |
| Contract | WSDL (strict, required) | Optional (OpenAPI/Swagger) |
| State | Can be stateful | Stateless |
| Performance | Heavier messages, no HTTP caching | Lightweight and cacheable |
| Security | WS-Security (message-level encryption and signing) | HTTPS/TLS, OAuth 2.0, JWT |
| Errors | `<soap:Fault>` element | HTTP status codes |
| Best for | Banking, payments, and enterprise/legacy integrations that need strict contracts and transactions | Web and mobile apps, public APIs, microservices |

## 5. Weather App (OpenWeatherMap)

The app is in [weather-app/](weather-app/): a Node.js + Express backend calls the OpenWeatherMap API and a web page shows the current temperature for any city. It applies the concepts above:
- The backend exposes its own REST endpoint, `GET /api/weather?city=London`, which returns JSON and uses status codes (200, 400, 404, 502).
- That endpoint consumes OpenWeatherMap's REST API.
- The API key stays on the server.

Setup and run instructions: [weather-app/README.md](weather-app/README.md)
