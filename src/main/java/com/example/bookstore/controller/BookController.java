package com.example.bookstore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.assembler.BookModelAssembler;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookExistException;
import com.example.bookstore.exception.InvalidAuthorNameException;
import com.example.bookstore.exception.InvalidIsbnException;
import com.example.bookstore.exception.InvalidTitleException;
import com.example.bookstore.repository.BookRepository;

@RestController
/**
 * To validate parameters that are passed into a method
 *
 */
@Validated
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookRepository repository;
	@Autowired
	private BookModelAssembler assembler;

	@GetMapping("/")
	public ResponseEntity<CollectionModel<EntityModel<Book>>> findAll() {
		return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));
	}

	@GetMapping("/isbn/{isbn}")
	public ResponseEntity<?> findOne(@PathVariable String isbn) {
		return repository.findById(isbn) //
				.map(assembler::toModel) //
				.map(ResponseEntity::ok) //
				.orElseThrow(() -> new InvalidIsbnException(isbn));
	}

	@GetMapping("/title/{title}")
	public ResponseEntity<CollectionModel<EntityModel<Book>>> findByTitle(@PathVariable String title) {
		List<Book> books = repository.findByTitleContaining(title) //
				.orElseThrow(() -> new InvalidTitleException(title));
		return ResponseEntity.ok(assembler.toCollectionModel(books));
	}

	@GetMapping("/author/{name}")
	public ResponseEntity<CollectionModel<EntityModel<Book>>> findByAuthorName(@PathVariable String name) {
		List<Book> books = repository.findByAuthorName(name) //
				.orElseThrow(() -> new InvalidAuthorNameException(name));
		return ResponseEntity.ok(assembler.toCollectionModel(books));
	}

	@PutMapping("/isbn/{isbn}")
	public ResponseEntity<?> updateBook(@RequestBody @Valid Book book, @PathVariable String isbn) {
		return repository.findById(isbn) //
				.map(b -> {
					b.setTitle(book.getTitle());
					b.setYear(book.getYear());
					b.setPrice(book.getPrice());
					b.setGenre(book.getGenre());
					book.getAuthors().forEach(a -> {
						b.addAuthor(a);
					});
					return repository.save(b);
				}).map(assembler::toModel) //
				.map(ResponseEntity::ok) //
				.orElseThrow(() -> new InvalidIsbnException(isbn));
	}

	@PostMapping("/")
	public ResponseEntity<?> newBook(@RequestBody @Valid Book book) {
		repository.findById(book.getIsbn()).ifPresent(b -> {
			throw new BookExistException(book.getIsbn());
		});
		return ResponseEntity.created(null) //
				.body(assembler.toModel(repository.save(book)));
	}

	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
		repository.findById(isbn).orElseThrow(() -> new InvalidIsbnException(isbn));
		repository.deleteById(isbn);
		return ResponseEntity.noContent().build();
	}

}
