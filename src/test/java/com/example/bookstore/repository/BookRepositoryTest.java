package com.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;

@DataJpaTest
class BookRepositoryTest {

	@Autowired
	private BookRepository repository;
	private Author expectedAuthor;
	private Book expectedBook;

	@BeforeEach
	void setUp() throws Exception {
		expectedAuthor = new Author("Colleen Hoover", "1977-02-10");
		expectedBook = new Book("1471156265", "It Ends with Us: A Novel", 2016, 9.49, "Romance Novel");
		expectedBook.addAuthor(expectedAuthor);
	}

	@Test
	void findByTitleContaining() {
		List<Book> bookList = repository.findByTitleContaining("US").get();
		assertThat(bookList).isNotEmpty();
		assertThat(bookList).first().isEqualTo(expectedBook);
	}

	@Test
	void findByAuthorName() {
		List<Book> bookList = repository.findByAuthorName("Colleen Hoover").get();
		assertThat(bookList).isNotEmpty();
		assertThat(bookList).first().isEqualTo(expectedBook);
	}

	@Test
	void findAll() {
		Iterable<Book> bookList = repository.findAll();
		assertThat(bookList).isNotEmpty();
		assertThat(bookList).contains(expectedBook);
	}

	@Test
	void findById() {
		Book result = repository.findById("1471156265").get();
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedBook);
	}

	@Test
	void replaceBook() {
		Book updatedBook = repository.findById("1471156265").get();
		updatedBook.setTitle("It Ends with Us: A Novel 3");
		updatedBook.setYear(20165);
		updatedBook.setPrice(95.49);
		updatedBook.setGenre("Romance Novel");
		repository.save(updatedBook);

		Book result = repository.findById("1471156265").get();
		assertThat(result).usingRecursiveComparison().isEqualTo(updatedBook);
	}

	@Test
	void replaceBookAndAuthor() {
		Book updatedBook = repository.findById("1471156265").get();
		updatedBook.setTitle("It Ends with Us: A Novel 3");
		updatedBook.setYear(20165);
		updatedBook.setPrice(95.49);
		updatedBook.setGenre("Romance Novel");

		Author updatedAuthor = new ArrayList<Author>(updatedBook.getAuthors()).get(0);
		updatedAuthor.setBirthday("1977-02-11");
		repository.save(updatedBook);

		Book result = repository.findById("1471156265").get();
		assertThat(result).usingRecursiveComparison().isEqualTo(updatedBook);
	}

	@Test
	void newBook() {
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11)", 2020, 25.99, "Science Fiction & Fantasy");
		newBook.addAuthor(newAuthor);
		repository.save(newBook);
		
		Book result = repository.findById("1250839378").get();
		assertThat(result).usingRecursiveComparison().isEqualTo(newBook);
	}

	@Test
	void deleteBook() {
		repository.delete(expectedBook);
		Book result = repository.findById("1471156265").orElse(null);
		assertThat(result).isNull();
	}

}
