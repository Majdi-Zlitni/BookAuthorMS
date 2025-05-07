package tn.esprit.bookservice.services.interfaces;

import tn.esprit.bookservice.dto.BookWithAuthorDto;
import tn.esprit.bookservice.entities.Book;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(String id);

    Book createBook(Book book);

    Optional<Book> updateBook(String id, Book bookDetails);

    boolean deleteBook(String id);

    // --- Operations Combining Book and Author Data ---

    List<BookWithAuthorDto> getBooksWithAuthors();

    Optional<BookWithAuthorDto> getBookWithAuthorById(String bookId);

    List<BookWithAuthorDto> getBooksByAuthorId(String authorId);
}
