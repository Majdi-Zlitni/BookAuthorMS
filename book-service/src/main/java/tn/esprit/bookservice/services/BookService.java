package tn.esprit.bookservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import Slf4j for logging
import org.springframework.stereotype.Service;
import tn.esprit.bookservice.dto.AuthorResponseDto;
import tn.esprit.bookservice.dto.BookWithAuthorDto;
import tn.esprit.bookservice.entities.Book;
import tn.esprit.bookservice.feign.AuthorClient;
import tn.esprit.bookservice.repository.BookRepository;
import tn.esprit.bookservice.services.interfaces.IBookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor // Injects dependencies via constructor
@Slf4j // Lombok annotation for easy logging (provides 'log' variable)
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final AuthorClient authorClient; // Feign client

    // --- Basic CRUD Operations ---

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(String id) {
        log.info("Fetching book by id: {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public Book createBook(Book book) {
        log.info("Creating new book for authorId: {}", book.getAuthorId());
        // Optional: Add validation here to check if authorId is valid by calling author-service
         try {
             authorClient.getAuthorById(book.getAuthorId());
         } catch (Exception e) {
            log.error("Author with ID {} not found or author-service unavailable.", book.getAuthorId(), e);
             throw new RuntimeException("Cannot create book: Author not found or service unavailable.");
         }

        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> updateBook(String id, Book bookDetails) {
        log.info("Attempting to update book with id: {}", id);
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book existingBook = bookOptional.get();
            log.info("Found book to update. AuthorId: {}", bookDetails.getAuthorId());
            // Optional: Validate new authorId if it changed
             if (!existingBook.getAuthorId().equals(bookDetails.getAuthorId())) {
                 try {
                     authorClient.getAuthorById(bookDetails.getAuthorId());
                 } catch (Exception e) {
                     log.error("New Author with ID {} not found or author-service unavailable.", bookDetails.getAuthorId(), e);
                     throw new RuntimeException("Cannot update book: New Author not found or service unavailable.");
                 }
             }
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setDescription(bookDetails.getDescription());
            existingBook.setAuthorId(bookDetails.getAuthorId());
            Book updatedBook = bookRepository.save(existingBook);
            log.info("Successfully updated book with id: {}", id);
            return Optional.of(updatedBook);
        } else {
            log.warn("Book with id {} not found for update.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteBook(String id) {
        log.info("Attempting to delete book with id: {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            log.info("Successfully deleted book with id: {}", id);
            return true;
        } else {
            log.warn("Book with id {} not found for deletion.", id);
            return false;
        }
    }

    // --- Operations Combining Book and Author Data ---

    @Override
    public List<BookWithAuthorDto> getBooksWithAuthors() {
        log.info("Fetching all books with their author details.");
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::mapToBookWithAuthorDTO) // Use helper method
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookWithAuthorDto> getBookWithAuthorById(String bookId) {
        log.info("Fetching book with author details for bookId: {}", bookId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        return bookOptional.map(this::mapToBookWithAuthorDTO); // Use helper method
    }

    @Override
    public List<BookWithAuthorDto> getBooksByAuthorId(String authorId) {
        log.info("Fetching books for authorId: {}", authorId);
        // First, check if author exists (optional but good practice)
        AuthorResponseDto author = fetchAuthorDetails(authorId); // Use helper method
        if (author == null) {
            log.warn("Author not found for authorId: {}, returning empty list.", authorId);
            return new ArrayList<>(); // Return empty list if author doesn't exist or service is down
        }

        List<Book> books = bookRepository.findByAuthorId(authorId); // Use custom repository method
        return books.stream()
                .map(book -> createBookWithAuthorDTO(book, author)) // Use the already fetched author
                .collect(Collectors.toList());
    }


    // --- Helper Methods ---

    /**
     * Fetches Author details from author-service using Feign client.
     * Includes basic error handling.
     */
    private AuthorResponseDto fetchAuthorDetails(String authorId) {
        try {
            log.debug("Fetching author details for authorId: {}", authorId);
            AuthorResponseDto author = authorClient.getAuthorById(authorId);
            log.debug("Successfully fetched author details for authorId: {}", authorId);
            return author;
        } catch (Exception e) {
            // Log the exception (FeignException contains details about the error)
            log.error("Error fetching author details for authorId {}: {}", authorId, e.getMessage());
            // Depending on requirements, you could throw a custom exception or return null/default
            return null; // Indicate that author details couldn't be fetched
        }
    }

    /**
     * Maps a Book entity to a BookWithAuthorDTO by fetching author details.
     */
    private BookWithAuthorDto mapToBookWithAuthorDTO(Book book) {
        AuthorResponseDto author = fetchAuthorDetails(book.getAuthorId());
        return createBookWithAuthorDTO(book, author);
    }

    /**
     * Creates a BookWithAuthorDTO from a Book and an AuthorResponseDto.
     * Handles cases where author details might be null (e.g., if fetch failed).
     */
    private BookWithAuthorDto createBookWithAuthorDTO(Book book, AuthorResponseDto author) {
        String authorName = (author != null) ? author.getName() : "Unknown Author";
        String authorEmail = (author != null) ? author.getEmail() : "N/A";
        String authorBio = (author != null) ? author.getBio() : "N/A"; // Include bio

        return new BookWithAuthorDto(
                authorName,
                authorEmail,
                authorBio, // Add bio
                book.getTitle(),
                book.getDescription(),
                book.getId() // Include book ID
        );
    }
}