package tn.esprit.bookservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.bookservice.dto.BookWithAuthorDto;
import tn.esprit.bookservice.entities.Book;
import tn.esprit.bookservice.services.BookService;

import jakarta.validation.Valid; // If you add validation to Book entity/DTO later
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books") // Base path for all book-related endpoints
@AllArgsConstructor // Injects BookService via constructor
public class BookController {

    private final BookService bookService;

    // --- Get All Books (Basic Info) ---
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // --- Get All Books (With Author Details) ---
    @GetMapping("/with-authors")
    public ResponseEntity<List<BookWithAuthorDto>> getAllBooksWithAuthors() {
        List<BookWithAuthorDto> booksWithAuthors = bookService.getBooksWithAuthors();
        return ResponseEntity.ok(booksWithAuthors);
    }

    // --- Get Single Book (Basic Info) ---
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok) // If found, wrap in 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // If not found, return 404
    }

    // --- Get Single Book (With Author Details) ---
    @GetMapping("/{id}/with-author")
    public ResponseEntity<BookWithAuthorDto> getBookWithAuthorById(@PathVariable String id) {
        return bookService.getBookWithAuthorById(id)
                .map(ResponseEntity::ok) // If found (and author fetch worked), wrap in 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // If book not found, return 404
    }

    // --- Get Books by Author ID (With Author Details) ---
    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<List<BookWithAuthorDto>> getBooksByAuthor(@PathVariable String authorId) {
        List<BookWithAuthorDto> books = bookService.getBooksByAuthorId(authorId);
        // We return OK even if the list is empty (e.g., author has no books, or author not found)
        return ResponseEntity.ok(books);
    }


    // --- Create Book ---
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // Consider adding @Valid if you have constraints on the Book entity/DTO
        // Ensure authorId is provided in the request body
        if (book.getAuthorId() == null || book.getAuthorId().isBlank()) {
            return ResponseEntity.badRequest().build(); // Return 400 if authorId is missing
        }
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook); // Return 201 Created
        } catch (RuntimeException e) {
            // Catch potential exception from service if author validation fails
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or return a proper error response DTO
        }
    }

    // --- Update Book ---
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book bookDetails) {
        // Consider adding @Valid
        if (bookDetails.getAuthorId() == null || bookDetails.getAuthorId().isBlank()) {
            return ResponseEntity.badRequest().build(); // Return 400 if authorId is missing
        }
        try {
            return bookService.updateBook(id, bookDetails)
                    .map(ResponseEntity::ok) // If updated, return 200 OK
                    .orElseGet(() -> ResponseEntity.notFound().build()); // If book not found, return 404
        } catch (RuntimeException e) {
            // Catch potential exception from service if author validation fails
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or return a proper error response DTO
        }
    }

    // --- Delete Book ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

}
