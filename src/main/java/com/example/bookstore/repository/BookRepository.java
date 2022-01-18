package com.example.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.bookstore.entity.Book;

public interface BookRepository extends CrudRepository<Book, String> {

	/**
	 * For wildcard expression binding, use CONCAT('%', ? '%')
	 */
	@Query("SELECT DISTINCT a FROM Book a JOIN FETCH a.authors b WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
	Optional<List<Book>> findByTitleContaining(String title);

	@Query("FROM Book a JOIN FETCH a.authors b WHERE LOWER(b.name) = LOWER(:name)")
	Optional<List<Book>> findByAuthorName(String name);

}
