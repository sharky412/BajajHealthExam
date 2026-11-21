# Bajaj Health Exam: Destination Hash Generator

My solution to a Bajaj Health exam question: a Java command-line tool that reads a JSON file, finds the first `destination` key, and prints an MD5 hash built from it.

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![JSON](https://img.shields.io/badge/org.json-20210307-000000?logo=json&logoColor=white)

## How it works

1. Takes two arguments: a PRN number and the path to a JSON file.
2. Normalises the PRN number (lowercase, whitespace removed).
3. Recursively walks the JSON (nested objects and arrays) and stops at the first `destination` key.
4. Generates a random 8-character string (URL-safe Base64 from `SecureRandom`).
5. Concatenates `PRN + destination value + random string` and computes its MD5 hash.
6. Prints the result as `<hash>;<randomString>`.

If no `destination` key is found, it prints a message and exits.

## Tech stack

- **Java** with `java.security.MessageDigest` (MD5) and `SecureRandom`
- **Maven** project (`DestinationHashGenerator/pom.xml`)
- **org.json** (`20210307`) for JSON parsing

## Getting started

The repo includes a runnable JAR with its dependency bundled:

```bash
git clone https://github.com/sharky412/BajajHealthExam.git
cd BajajHealthExam
java -jar DestinationHashGenerator.jar <PRN Number> <path/to/file.json>
```

Example input file:

```json
{ "student": { "details": { "destination": "Pune" } } }
```

To build from source instead, open `DestinationHashGenerator/` as a Maven project (it was created in Eclipse).

## Project structure

```
BajajHealthExam/
├── DestinationHashGenerator.jar          # Runnable JAR
└── DestinationHashGenerator/
    ├── pom.xml
    └── src/main/java/com/app/HashGenerator/
        └── DestinationHashGenerator.java # Main class
```
