package com.example.bookstore.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bookstore.assembler.BookModelAssembler;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
@Import({ BookModelAssembler.class })
class BookControllerTest {
	/**
	 * Method naming convention: [UnitOfWork_StateUnderTest_ExpectedBehavior]. //
	 * Use Given-When-Then style
	 */

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BookRepository repository;

	private Author mockAuthor1;
	private Author mockAuthor2;
	private Author mockAuthor3;
	private Book mockBook1;
	private Book mockBook2;
	private Book mockBook3;
	private List<Book> mockBookList;

	private final String baseUrl = "http://localhost/books/";
	private final String isbnUrl = baseUrl + "isbn/";

	@BeforeEach
	void setUp() throws Exception {
		mockAuthor1 = new Author("Colleen Hoover", "1977-02-10");
		mockAuthor2 = new Author("J. K. Rowling", "1968-02-10");
		mockAuthor3 = new Author("Jim Field", "1958-02-10");

		mockBook1 = new Book("1471156265", "It Ends with Us: A Novel", 2016, 9.49, "Romance Novel");
		mockBook2 = new Book("1408711702", "Fantastic Beasts: The Crimes of Grindelwald", 2018, 19.99,
				"Contemporary Fiction");
		mockBook3 = new Book("1338790234", "The Christmas Pig", 2021, 12.99, "Children's Literature");

		mockBook1.addAuthor(mockAuthor1);
		mockBook2.addAuthor(mockAuthor2);
		mockBook3.addAuthor(mockAuthor2);
		mockBook3.addAuthor(mockAuthor3);

		mockBookList = new ArrayList<Book>();
	}

	@Test
	public void findAll_FetchAllBooks() throws Exception {
		// Given
		mockBookList.add(mockBook1);
		mockBookList.add(mockBook2);
		mockBookList.add(mockBook3);
		given(repository.findAll()).willReturn(mockBookList);

		// When
		mockMvc.perform(get("/books/")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$._embedded.bookList[0].isbn", is(mockBook1.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].title", is(mockBook1.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].year", is(mockBook1.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].price", is(mockBook1.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].genre", is(mockBook1.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].name", is(mockAuthor1.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].birthday", is(mockAuthor1.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.self.href", is(isbnUrl + mockBook1.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._embedded.bookList[1].isbn", is(mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].title", is(mockBook2.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].year", is(mockBook2.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].price", is(mockBook2.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].genre", is(mockBook2.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[0].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[0].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[1]._links.self.href", is(isbnUrl + mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[1]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._embedded.bookList[2].isbn", is(mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].title", is(mockBook3.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].year", is(mockBook3.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].price", is(mockBook3.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].genre", is(mockBook3.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].authors[0].name", is(mockAuthor3.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].authors[0].birthday", is(mockAuthor3.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].authors[1].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[2].authors[1].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[2]._links.self.href", is(isbnUrl + mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[2]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void findOne_ValidIsbn_FetchSelectedBook() throws Exception {
		// Given
		given(repository.findById(mockBook2.getIsbn())).willReturn(Optional.of(mockBook2));

		// When
		mockMvc.perform(get("/books/isbn/" + mockBook2.getIsbn())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.isbn", is(mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$.title", is(mockBook2.getTitle()))) //
				.andExpect(jsonPath("$.year", is(mockBook2.getYear()))) //
				.andExpect(jsonPath("$.price", is(mockBook2.getPrice()))) //
				.andExpect(jsonPath("$.genre", is(mockBook2.getGenre()))) //
				.andExpect(jsonPath("$.authors[0].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$.authors[0].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._links.self.href", is(isbnUrl + mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void findOne_InvalidIsbn_ReturnErrorMessage() throws Exception {
		// Given
		final String isbn = "1432156265";
		given(repository.findById(isbn)).willReturn(Optional.empty());

		// When
		mockMvc.perform(get("/books/isbn/" + isbn)) //
				.andExpect(status().isNotFound()) //
				.andExpect(jsonPath("$.error", is("Could not find book by isbn " + isbn))) //
				.andExpect(jsonPath("$.status", is(NOT_FOUND.toString())));
	}

	@Test
	public void findByTitle_MatchingKeyword_FetchMatchedBooks() throws Exception {
		// Given
		final String title = "The";
		mockBookList.add(mockBook2);
		mockBookList.add(mockBook3);
		given(repository.findByTitleContaining(title)).willReturn(Optional.of(mockBookList));

		// When
		mockMvc.perform(get("/books/title/" + title)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$._embedded.bookList[0].isbn", is(mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].title", is(mockBook2.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].year", is(mockBook2.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].price", is(mockBook2.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].genre", is(mockBook2.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.self.href", is(isbnUrl + mockBook2.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._embedded.bookList[1].isbn", is(mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].title", is(mockBook3.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].year", is(mockBook3.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].price", is(mockBook3.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].genre", is(mockBook3.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[0].name", is(mockAuthor3.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[0].birthday", is(mockAuthor3.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[1].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[1].authors[1].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[1]._links.self.href", is(isbnUrl + mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[1]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void findByTitle_NonMatchingKeyword_ReturnErrorMessage() throws Exception {
		// Given
		final String title = "Harry Potter";
		given(repository.findByTitleContaining(title)).willReturn(Optional.empty());

		// When
		mockMvc.perform(get("/books/title/" + title)) //
				.andExpect(status().isNotFound()) //
				.andExpect(jsonPath("$.error", is("Could not find book by title " + title))) //
				.andExpect(jsonPath("$.status", is(NOT_FOUND.toString())));
	}

	@Test
	public void findByAuthorName_ExactMatch_FetchMatchedBooks() throws Exception {
		// Given
		final String name = "Jim Field";
		mockBookList.add(mockBook3);
		given(repository.findByAuthorName(name)).willReturn(Optional.of(mockBookList));

		// When
		mockMvc.perform(get("/books/author/" + name)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$._embedded.bookList[0].isbn", is(mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].title", is(mockBook3.getTitle()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].year", is(mockBook3.getYear()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].price", is(mockBook3.getPrice()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].genre", is(mockBook3.getGenre()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].name", is(mockAuthor3.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[0].birthday", is(mockAuthor3.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[1].name", is(mockAuthor2.getName()))) //
				.andExpect(jsonPath("$._embedded.bookList[0].authors[1].birthday", is(mockAuthor2.getBirthday()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.self.href", is(isbnUrl + mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$._embedded.bookList[0]._links.books.href", is(baseUrl))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void findByAuthorName_NonMatching_ReturnErrorMessage() throws Exception {
		// Given
		final String name = "Rowling";
		given(repository.findByAuthorName(name)).willReturn(Optional.empty());

		// When
		mockMvc.perform(get("/books/author/" + name)) //
				.andExpect(status().isNotFound()) //
				.andExpect(jsonPath("$.error", is("Could not find book by author name " + name))) //
				.andExpect(jsonPath("$.status", is(NOT_FOUND.toString())));
	}

	@Test
	public void updateBook_SimpleValues_FetchUpdatedBook() throws Exception {
		// Given
		final String isbn = "1471156265";
		Book newBook = new Book("1471156265", "It Doesn't End: A Novel", 2016, 19.49, "Romance Novel");
		newBook.addAuthor(mockAuthor1);
		given(repository.findById(mockBook1.getIsbn())).willReturn(Optional.of(mockBook1));
		given(repository.save(newBook)).willReturn(newBook);

		// When
		mockMvc.perform(put("/books/isbn/" + isbn) //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(jsonPath("$.isbn", is(newBook.getIsbn()))) //
				.andExpect(jsonPath("$.title", is(newBook.getTitle()))) //
				.andExpect(jsonPath("$.year", is(newBook.getYear()))) //
				.andExpect(jsonPath("$.price", is(newBook.getPrice()))) //
				.andExpect(jsonPath("$.genre", is(newBook.getGenre()))) //
				.andExpect(jsonPath("$.authors[0].name", is(mockAuthor1.getName()))) //
				.andExpect(jsonPath("$.authors[0].birthday", is(mockAuthor1.getBirthday()))) //
				.andExpect(jsonPath("$._links.self.href", is(isbnUrl + newBook.getIsbn()))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void updateBook_InvalidIsbn_ReturnErrorMessage() throws Exception {
		// Given
		final String isbn = "1471156267";
		Book newBook = new Book("1471156267", "It Doesn't End: A Novel", 2016, 19.49, "Romance Novel");
		newBook.addAuthor(mockAuthor1);
		given(repository.findById(mockBook1.getIsbn())).willReturn(Optional.empty());

		// When
		mockMvc.perform(put("/books/isbn/" + isbn) //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isNotFound()) //
				.andExpect(jsonPath("$.error", is("Could not find book by isbn " + isbn))) //
				.andExpect(jsonPath("$.status", is(NOT_FOUND.toString())));
	}

	@Test
	public void newBook_SimpleValues_FetchNewBook() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 2022, 25.49,
				"Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);
		given(repository.findById(newBook.getIsbn())).willReturn(Optional.empty());
		given(repository.save(newBook)).willReturn(newBook);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.isbn", is(newBook.getIsbn()))) //
				.andExpect(jsonPath("$.title", is(newBook.getTitle()))) //
				.andExpect(jsonPath("$.year", is(newBook.getYear()))) //
				.andExpect(jsonPath("$.price", is(newBook.getPrice()))) //
				.andExpect(jsonPath("$.genre", is(newBook.getGenre()))) //
				.andExpect(jsonPath("$.authors[0].name", is(newAuthor.getName()))) //
				.andExpect(jsonPath("$.authors[0].birthday", is(newAuthor.getBirthday()))) //
				.andExpect(jsonPath("$._links.self.href", is(isbnUrl + newBook.getIsbn()))) //
				.andExpect(jsonPath("$._links.books.href", is(baseUrl)));
	}

	@Test
	public void newBook_ExistingIsbn_ReturnErrorMessage() throws Exception {
		// Given
		given(repository.findById(mockBook3.getIsbn())).willReturn(Optional.of(mockBook3));

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(mockBook3))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error", is("Existing book by isbn " + mockBook3.getIsbn()))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_BlankIsbn_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("", "Quantum of Nightmares (Laundry Files, 11", 2022, 25.49,
				"Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.isbn", is("ISBN is mandatory"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_BlankTitle_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "", 2022, 25.49, "Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.title", is("Title is mandatory"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_YearOutOfRange_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1000, 25.49,
				"Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.year", is("Year must be between 1900 to 2999"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_PriceLEMin_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1990, -1,
				"Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.price", is("Minimium price must be > 0"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_PriceGEMax_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1990, 19999.99,
				"Science Fiction & Fantasy");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.price", is("Maxmium price must be <= 9999.99"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_BlankGenre_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1990, 11.99, "");
		Author newAuthor = new Author("Charles Stross", "1953-12-10");
		newBook.addAuthor(newAuthor);

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.genre", is("Genre is mandatory"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

	@Test
	public void newBook_EmptyAuthor_ReturnErrorMessage() throws Exception {
		// Given
		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1990, 11.99,
				"Science Fiction & Fantasy");

		// When
		mockMvc.perform(post("/books/") //
				.contentType(APPLICATION_JSON_VALUE) //
				.accept(APPLICATION_JSON) //
				.characterEncoding("UTF-8") //
				.content(new ObjectMapper().writeValueAsString(newBook))) //
				.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.error.authors", is("Author list cannot be empty"))) //
				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
	}

//	@Test
//	public void newBook_BlankAuthorName_ReturnErrorMessage() throws Exception {
//		// Given
//		Book newBook = new Book("1250839378", "Quantum of Nightmares (Laundry Files, 11", 1990, 11.99,
//				"Science Fiction & Fantasy");
//		Author newAuthor = new Author("", "1953-12-10");
//		newBook.addAuthor(newAuthor);
//
//		// When
//		mockMvc.perform(post("/books/") //
//				.contentType(APPLICATION_JSON_VALUE) //
//				.accept(APPLICATION_JSON) //
//				.characterEncoding("UTF-8") //
//				.content(new ObjectMapper().writeValueAsString(newBook))) //
//				.andExpect(status().isBadRequest()) //
//				.andExpect(jsonPath("$.error.authors[].name", is("Author name is mandatory"))) //
//				.andExpect(jsonPath("$.status", is(BAD_REQUEST.toString())));
//	}

	@Test
	public void deleteBook_ValidIsbn_Successful() throws Exception {
		// Given
		given(repository.findById(mockBook3.getIsbn())).willReturn(Optional.of(mockBook3));

		// When
		mockMvc.perform(delete("/books/isbn/" + mockBook3.getIsbn())) //
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void deleteBook_InvalidIsbn_ReturnErrorMessage() throws Exception {
		// Given
		final String isbn = "1432156265";
		given(repository.findById(isbn)).willReturn(Optional.empty());

		// When
		mockMvc.perform(delete("/books/isbn/" + isbn)) //
				.andExpect(status().isNotFound()) //
				.andExpect(jsonPath("$.error", is("Could not find book by isbn " + isbn))) //
				.andExpect(jsonPath("$.status", is(NOT_FOUND.toString())));
	}

}
