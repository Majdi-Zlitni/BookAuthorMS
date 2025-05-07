package tn.esprit.authorservice.services.interfaces;

import tn.esprit.authorservice.dto.AuthorRequestDto;
import tn.esprit.authorservice.dto.AuthorResponseDto;
import tn.esprit.authorservice.entities.Author;

import java.util.List;
import java.util.Optional;

public interface IAuthorService {
    List<AuthorResponseDto> getAllAuthors();

    Optional<AuthorResponseDto> getAuthorById(String id);

    AuthorResponseDto createAuthor(AuthorRequestDto authorDto);

    Optional<AuthorResponseDto> updateAuthor(String id, AuthorRequestDto authorDto);

    boolean deleteAuthor(String id);

    Optional<Author> findAuthorEntityById(String id);
    String getWelcomeMessage();
}
