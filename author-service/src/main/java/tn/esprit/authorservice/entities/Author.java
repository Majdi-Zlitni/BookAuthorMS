package tn.esprit.authorservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "authors")
public class Author {
    @Id
    private String id;
    private String name;
    private String email;
    private String bio;
}