package com.example.bookstore.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@Entity
@Table(name = "book")
public class Book {

	@Id
	@Column(name = "isbn", unique = true, nullable = false)
	@NotBlank(message = "ISBN is mandatory")
	private String isbn;

	@Column(name = "title")
	@NotBlank(message = "Title is mandatory")
	private String title;

	@Column(name = "year")
	@Range(min = 1900, max = 2999, message = "Year must be between 1900 to 2999")
	private int year;

	@Column(name = "price")
	@DecimalMin(value = "0.0", inclusive = false, message = "Minimium price must be > 0")
	@DecimalMax(value = "9999.99", inclusive = true, message = "Maxmium price must be <= 9999.99")
	private double price;

	@Column(name = "genre")
	@NotBlank(message = "Genre is mandatory")
	private String genre;

	/**
	 * Propagates the persist/merge operation from a parent to a child entity. //
	 * When we save the book entity, the author entity will also get saved.
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "book_author", //
			joinColumns = { @JoinColumn(name = "book_isbn") }, //
			inverseJoinColumns = { @JoinColumn(name = "author_name") })
	@NotEmpty(message = "Author list cannot be empty")
	private Set<@Valid Author> authors = new HashSet<Author>();

	public Book() {
		super();
	}

	public Book(String isbn, String title, int year, double price, String genre) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.year = year;
		this.price = price;
		this.genre = genre;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(final Set<Author> authors) {
		this.authors = authors;
	}

	public void addAuthor(Author a) {
		this.authors.add(a);
		a.getBooks().add(this);
	}

	public void removeAuthor(Author a) {
		this.authors.remove(a);
		a.getBooks().remove(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(isbn, other.isbn);
	}

}
