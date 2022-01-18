package com.example.bookstore.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "author")
public class Author {

	@Id
	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Author name is mandatory")
	private String name;

	@Column(name = "birthday")
	@NotBlank(message = "Author birthday is mandatory")
	private String birthday;

	/**
	 * To ignore one side of the relationship, thus breaking the chain
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "authors")
	private Set<Book> books = new HashSet<Book>();

	public Author() {
		super();
	}

	public Author(String name, String birthday) {
		super();
		this.name = name;
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Set<Book> getBooks() {
		return this.books;
	}

	public void setBooks(final Set<Book> books) {
		this.books = books;
	}

	public void addBook(Book b) {
		this.books.add(b);
		b.getAuthors().add(this);
	}

	public void removeBook(Book b) {
		this.books.remove(b);
		b.getAuthors().remove(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(name, other.name);
	}

}
