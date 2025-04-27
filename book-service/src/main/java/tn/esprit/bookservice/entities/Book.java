package tn.esprit.bookservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "books") // Specifies the MongoDB collection name
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all arguments
public class Book {

    @Id // Marks this field as the primary identifier in MongoDB
    private String id;

    private String title;
    private String description;

    // This field will store the ID of the author associated with this book.
    // It references an author managed by the author-service.
    private String authorId;
}