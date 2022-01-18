CREATE TABLE book (
    isbn  VARCHAR(128) NOT NULL,
    title VARCHAR(128)         ,
    year  INT			       ,
    price DOUBLE               ,
    genre VARCHAR(128)         ,
    PRIMARY KEY (isbn)
);
CREATE TABLE author (
    name   	 VARCHAR(128) NOT NULL,
    birthday VARCHAR(128) 		  ,
    PRIMARY KEY (name)
);
CREATE TABLE book_author (
    book_isbn   VARCHAR(128) NOT NULL,
    author_name VARCHAR(128) NOT NULL,
    CONSTRAINT FK_BOOK_ISBN   FOREIGN KEY(book_isbn)   REFERENCES   book(isbn),
	CONSTRAINT FK_AUTHOR_NAME FOREIGN KEY(author_name) REFERENCES author(name),
	PRIMARY KEY(book_isbn, author_name)        
);