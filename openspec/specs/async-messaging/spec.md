# Async Messaging Specification

## Purpose
Offload long-running work from request threads and enable decoupled, asynchronous communication using both simple async functions and a RabbitMQ message broker. Endpoints live under `/api/v1/async` and `/api/v1/ms-broker`.

## Requirements

### Requirement: Fire-and-forget async execution
The system SHALL execute designated operations asynchronously so the HTTP request returns without waiting for the work to finish, governed by a configurable worker pool and queue capacity.

#### Scenario: Trigger an async task
- **WHEN** a client calls `POST /api/v1/async/send-dummy-email`
- **THEN** the system accepts the request, schedules the work on a background worker, and returns immediately

### Requirement: Trackable async jobs
The system SHALL assign a job identifier to async work so a client can query its status later via `GET /api/v1/async/status-dummy-email-job-id`.

#### Scenario: Submit and poll a job
- **WHEN** a client submits a job via `POST /api/v1/async/send-dummy-email-job-id` and later polls the status endpoint with the returned job id
- **THEN** the system returns the current status of that job

### Requirement: RabbitMQ-backed async jobs
The system SHALL support dispatching async jobs through RabbitMQ via `POST /api/v1/async/send-dummy-email-job-id-rabbitmq`, so the work is processed by a consumer off the message queue.

#### Scenario: Dispatch via RabbitMQ
- **WHEN** a client submits a RabbitMQ-backed job
- **THEN** the system enqueues a message and a consumer processes it asynchronously

### Requirement: Message broker publish
The system SHALL allow publishing messages to the broker via `/api/v1/ms-broker`, supporting both object and plain-text payloads on a topic.

#### Scenario: Publish an object message
- **WHEN** a client calls `POST /api/v1/ms-broker/topic/object` with a valid payload
- **THEN** the system publishes the message to the configured topic for downstream consumers

#### Scenario: Publish a text message
- **WHEN** a client calls `POST /api/v1/ms-broker/topic/text` with a text payload
- **THEN** the system publishes the text message to the configured topic
