package tn.esprit.authorservice.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.authorservice.entities.Author;

import java.util.Optional;
public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findByEmail(String email);
}
