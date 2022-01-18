package com.example.bookstore.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.bookstore.controller.BookController;
import com.example.bookstore.entity.Book;

@Component
public class BookModelAssembler implements SimpleRepresentationModelAssembler<Book> {

	@Override
	public void addLinks(EntityModel<Book> resource) {
		resource.add(linkTo(methodOn(BookController.class).findOne(resource.getContent().getIsbn())).withSelfRel());
		resource.add(linkTo(methodOn(BookController.class).findAll()).withRel("books"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<Book>> resources) {
		resources.add(linkTo(methodOn(BookController.class).findAll()).withRel("books"));
	}

}
