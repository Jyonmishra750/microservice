# digiBank Microservices

A modular banking microservices architecture built with **Java 25** and **Spring Boot 4.1.1**.

## Services
* **`accounts`** (Port `8081`) — Customer & Bank Account management, CRUD REST APIs, JPA auditing, and OpenAPI documentation.
* * **`cards`** *(Port `8082`)* — Credit/Debit card management service.
* **`loans`** *(Port `8083`)* — Loan origination and management service.

## Tech Stack
* **Core:** Java 25, Spring Boot 4.1.1, Spring Web, Spring Data JPA, Jakarta Validation
* **Database:** H2 (File-based local persistence)
* **Resilience & Observability:** Spring Cloud Circuit Breaker (Resilience4j), Spring Boot Actuator
* **Documentation & Tooling:** SpringDoc OpenAPI (Swagger UI), Lombok, Maven