# Les Sagas MP3 - core
[![Release](https://github.com/les-sagas-mp3/core/workflows/Release/badge.svg)](https://github.com/les-sagas-mp3/core/actions?query=workflow%3ARelease)
[![Build](https://github.com/Les-Sagas-MP3/core/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Les-Sagas-MP3/core/actions/workflows/build.yml)

## Prerequisites

- Java 19 - [Download here](https://jdk.java.net/19/)
- PostgreSQL 12 - [Download here](https://www.postgresql.org/download/)
- Maven 3 - [Download here](https://maven.apache.org/download.cgi)
- (Optional) A PostgreSQL Client - [Download DBeaver here](https://dbeaver.io/download/)

## Getting Started

1. Create a PostgreSQL database matching the configuration in `src/main/resources/application.properties`
2. Create the environment variables :
   - `FR_LESSAGASMP3_CORE_URL` : URL of this app
   - `CLOUDINARY_URL` : URL of Cloudinary account
   - `FIREBASE_URL` : URL of Firebase database
   - `GOOGLE_APPLICATION_CREDENTIALS` : Path to the Firebase admin SDK private key 
3. Run the following command :
```bash
mvn clean install spring-boot:run
```
