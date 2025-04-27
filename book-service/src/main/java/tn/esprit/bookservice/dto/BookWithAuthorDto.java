package tn.esprit.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookWithAuthorDto {
    private String authorName;
    private String authorEmail;
    private String authorBio; // Added based on your update

    // Book Details (from Book entity)
    private String bookTitle;
    private String bookDescription;
    private String bookId; // Optional: Include book ID if needed by the client

}
