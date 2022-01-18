# Bookstore-SpringBoot-JPA-HATEOAS
This project depicts the Spring Boot Example with Spring Data JPA and HATEOAS Example

## Libraries used
- Spring Boot Starter Data Jpa
- Spring Boot Starter Hateoas
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Jackson Databind
- H2
- Junit 5

## REST APIs
#### Create Book
Adds a new book to the bookstore.\
`curl -v -X POST localhost:8080/books/ -H 'Content-Type:application/json' -d '{"isbn":"1250839378","title":"Quantum of Nightmares (Laundry Files, 11)","year":2022,"price":25.99,"genre":"Science Fiction & Fantasy","authors":[{"name":"Charles Stross","birthday":"1953-12-10"}]}'`

#### Search Book
Title search allow for partial matches.\
`curl -v -X GET localhost:8080/books/title/The`

Author search is strictly exact match.\
`curl -v -X GET localhost:8080/books/author/J.%20K.%20Rowling`

#### Update Book
Update a existing book from the bookstore.\
`curl -v -X PUT localhost:8080/books/isbn/1250839378 -H 'Content-Type:application/json' -d '{"isbn":"1250839378","title":"Quantum of Nightmares (Laundry Files, 11)","year":2022,"price":25.99,"genre":"Science Fiction & Fantasy","authors":[{"name":"Charles Stross","birthday":"1953-12-10"}]}'`

#### Delete Book
Delete a existing book from the bookstore.\
`curl -v -X DELETE localhost:8080/books/isbn/1338790234`

