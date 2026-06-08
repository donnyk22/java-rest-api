# Data Export & Import Specification

## Purpose
Allow users to export application data into common office formats (Excel `.xlsx`, Word `.docx`, ZIP `.zip` archives) and to import data from Excel, supporting reporting and data exchange workflows. Endpoints live under `/api/v1/excel`, `/api/word`, and `/api/v1/zip`.

## Requirements

### Requirement: Excel export
The system SHALL export data to `.xlsx` files via `/api/v1/excel`, including generation from scratch and from a pre-existing template.

#### Scenario: Export student data to Excel
- **WHEN** an authorized client calls `GET /api/v1/excel/export-student-data`
- **THEN** the system returns an `.xlsx` file containing the student data with the correct spreadsheet content type

#### Scenario: Export using an existing template
- **WHEN** an authorized client calls `GET /api/v1/excel/export-with-existing-template`
- **THEN** the system fills the template and returns the resulting `.xlsx` file

### Requirement: Excel import
The system SHALL accept an uploaded `.xlsx` file via `POST /api/v1/excel/import-excel-file` (multipart), parse its rows, and return the parsed result as JSON.

#### Scenario: Import a valid spreadsheet
- **WHEN** an authorized client uploads a well-formed `.xlsx` file
- **THEN** the system parses the rows and returns the extracted data as JSON

#### Scenario: Import an invalid file
- **WHEN** the uploaded file is not a valid spreadsheet
- **THEN** the system rejects the request with a validation error

### Requirement: Word export
The system SHALL generate `.docx` documents via `/api/word`, including example generation, template-based generation, and student-data export.

#### Scenario: Export student data to Word
- **WHEN** an authorized client calls `GET /api/word/export-student-data`
- **THEN** the system returns a `.docx` document containing the student data with the correct document content type

### Requirement: Compressed (ZIP) export
The system SHALL produce `.zip` archives via `/api/v1/zip`, supporting both in-memory generation and on-disk generation.

#### Scenario: Generate a ZIP in memory
- **WHEN** an authorized client calls `GET /api/v1/zip/generate-in-memory`
- **THEN** the system streams back a `.zip` archive with the correct archive content type
