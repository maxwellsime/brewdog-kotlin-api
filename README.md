# Punk API

Kotlin Project exposing the now defunct [Brewdog Punk API](https://punkapi.com/)<sup>[1]</sup> into HTTP endpoints using Ktor. Inputs and outputs in JSON format.

Uses: Gradle, Kotlin, Koin for dependency injecting, Ktor server/client framework, Kotest and mockk for testing.

## Usage
### POST /beers
Request:
```json
{
  "name": "beer name"
}
```
Response:
```json
[
  {
    "id": 1,
    "name": "beer name 1",
    "description": "beer description"
  },
  {
    "id": 2,
    "name": "beer name 2",
    "description": "beer description"
  }
]
```
OR

Request:
```json
{
  "page": 1
}
```
Response:
```json
[
  {
    "id": 1,
    "name": "beer name 1",
    "description": "beer description"
  },
  {
    "id": 2,
    "name": "beer name 2",
    "description": "beer description"
  }
]
```

### GET /beers/id
Response:
```json
{
    "id": 1,
    "name": "beer name 1",
    "description": "beer description"
}
```

### Run locally
Use main found in Application.kt, or Gradle .run

# Bibliography
[1]["punkapi"; Sammdec & Other contributors, 2016-2024;](https://github.com/sammdec/punkapi)