# Library API

## Stack used

- Java 11
- Gradle 6.6
- Spring Boot 2.3.4

## Start
To start this project, simply run

```
./gradlew bootRun
``` 

To see the DB, go to `localhost:8080/h2-console`

Params:

```
JDBC URL: jdbc:h2:mem:library
Username: sa
Pass:

```

## Sample requests

#### Successful request
HTTP Status: 200
```
curl localhost:8080/books/BOOK-GRUFF472
``` 

#### Invalid book reference
HTTP Status: 400

```
curl localhost:8080/books/INVALID-TEXT
``` 

#### Book not found
HTTP Status: 404
```
curl localhost:8080/books/BOOK-LOSTDIALOGUE
``` 
