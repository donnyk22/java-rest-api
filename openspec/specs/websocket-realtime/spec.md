# WebSocket Realtime Specification

## Purpose
Provide real-time, bidirectional communication between server and clients (e.g., notifications, presence) over WebSocket, with authenticated connections. Supporting REST endpoints live under `/api/v1/ws`.

## Requirements

### Requirement: Authenticated WebSocket connections
The system SHALL require a valid authentication token to establish a WebSocket session and SHALL reject unauthenticated connection attempts.

#### Scenario: Connect with valid token
- **WHEN** a client opens a WebSocket connection presenting a valid token
- **THEN** the system accepts and registers the connection for the authenticated user

#### Scenario: Connect without valid token
- **WHEN** a client attempts to open a WebSocket connection without a valid token
- **THEN** the system rejects the connection

### Requirement: Server-initiated messaging
The system SHALL allow pushing messages to connected clients, including broadcast and per-user delivery, via `POST /api/v1/ws` and `POST /api/v1/ws/users`.

#### Scenario: Send a message to a user
- **WHEN** an authorized client calls `POST /api/v1/ws/users` targeting a connected user
- **THEN** the system delivers the message to that user's active WebSocket session(s)

### Requirement: Online presence
The system SHALL expose the set of currently connected users via `GET /api/v1/ws/users/online`.

#### Scenario: Query online users
- **WHEN** an authorized client requests the online users endpoint
- **THEN** the system returns the list of currently connected users
