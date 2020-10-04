# Les Sagas MP3 - core
[![Release](https://github.com/les-sagas-mp3/core/workflows/Release/badge.svg)](https://github.com/les-sagas-mp3/core/actions?query=workflow%3ARelease)
[![Integration](https://github.com/les-sagas-mp3/core/workflows/Integration/badge.svg)](https://github.com/les-sagas-mp3/core/actions?query=workflow%3AIntegration)

## Prerequisites

- Java 14 - [Download here](https://jdk.java.net/14/)
- PostgreSQL 12 - [Download here](https://www.postgresql.org/download/)
- A PostgreSQL Client - [Download DBeaver here](https://dbeaver.io/download/)
- Maven 3 - [Download here](https://maven.apache.org/download.cgi)

## Getting Started

1. Create a PostgreSQL database matching the configuration in `src/main/resources/application.properties`
2. Create the environment variables :
  - `FIREBASE_URL` : the Firebase database URL
  - `GOOGLE_APPLICATION_CREDENTIALS` : path to the Firebase admin SDK private key 
3. Run the following command :
```bash
mvn clean install spring-boot:run
```