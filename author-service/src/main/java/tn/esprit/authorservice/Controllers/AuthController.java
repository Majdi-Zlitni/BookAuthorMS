package tn.esprit.authorservice.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.authorservice.dto.AuthorRequestDto;   // Import DTO
import tn.esprit.authorservice.dto.AuthorResponseDto;  // Import DTO
import tn.esprit.authorservice.entities.Author; // Keep for internal use if needed, or remove if service handles all
import tn.esprit.authorservice.services.AuthorService;
import tn.esprit.authorservice.services.interfaces.IAuthorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthController {
    private final IAuthorService authorService;

    @GetMapping("/welcome") // Make sure this is present and correct
    public ResponseEntity<String> getServiceWelcomeMessage() {
        return ResponseEntity.ok(authorService.getWelcomeMessage());
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        List<AuthorResponseDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable String id) {
        Optional<AuthorResponseDto> authorDto = authorService.getAuthorById(id);
        return authorDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<Author> getAuthorEntityByIdForInternal(@PathVariable String id) {
        Optional<Author> author = authorService.findAuthorEntityById(id); // Assuming service has this method
        return author.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        AuthorResponseDto createdAuthor = authorService.createAuthor(authorRequestDto);
        // Consider returning 201 Created status with location header
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable String id, @RequestBody AuthorRequestDto authorRequestDto) {
        Optional<AuthorResponseDto> updatedAuthor = authorService.updateAuthor(id, authorRequestDto);
        return updatedAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
        boolean deleted = authorService.deleteAuthor(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
