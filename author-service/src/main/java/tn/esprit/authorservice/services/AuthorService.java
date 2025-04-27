package tn.esprit.authorservice.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.authorservice.dto.AuthorRequestDto;
import tn.esprit.authorservice.dto.AuthorResponseDto;
import tn.esprit.authorservice.entities.Author;
import tn.esprit.authorservice.mapper.AuthorMapper;
import tn.esprit.authorservice.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {
    private AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorResponseDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.listEntityToResponseDto(authors); // Use mapper
    }

    public Optional<AuthorResponseDto> getAuthorById(String id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        return authorOptional.map(authorMapper::entityToResponseDto);
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = authorMapper.requestDtoToEntity(authorRequestDto); // Use mapper
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.entityToResponseDto(savedAuthor); // Use mapper
    }

    public Optional<AuthorResponseDto> updateAuthor(String id, AuthorRequestDto authorRequestDto) {
        Optional<Author> existingAuthorOptional = authorRepository.findById(id);
        if (existingAuthorOptional.isPresent()) {
            Author existingAuthor = existingAuthorOptional.get();
            // Update fields from DTO (manual update or use @MappingTarget in mapper)
            existingAuthor.setName(authorRequestDto.getName());
            existingAuthor.setEmail(authorRequestDto.getEmail());
            existingAuthor.setBio(authorRequestDto.getBio());

            Author updatedAuthor = authorRepository.save(existingAuthor);
            return Optional.of(authorMapper.entityToResponseDto(updatedAuthor));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAuthor(String id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        } else {
            return false; // Author not found
        }
    }

    public Optional<Author> findAuthorEntityById(String id) {
        return authorRepository.findById(id);
    }

}
