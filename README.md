# AnonML Document Management Service

* Upload DOCX / PDF
* Retrieve Plaintext
* Download anonimized file

## API

| Method | Path                  | Result | Comment |
|--------|-----------------------|--------|---------|
| GET    | /document/{id}        |the document (id, plain text)|         |
| POST   | /document/import             | 201, link to document in header|expected parameter is the file|
| POST   | /document/{id}/export | - |         |
| GET | /document/{id}/htmlView | HTML view of the document | only for pdf documents atm. |







