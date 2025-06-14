package tn.esprit.authorservice.mapper;
import org.mapstruct.Mapper;
import tn.esprit.authorservice.dto.AuthorRequestDto;
import tn.esprit.authorservice.dto.AuthorResponseDto;
import tn.esprit.authorservice.entities.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    // Maps Author entity to AuthorResponseDto


    Author authorRequestDtoToAuthor(AuthorRequestDto authorRequestDto);

    // This is the method you want to keep for single entity to DTO mapping
    AuthorResponseDto authorToAuthorResponseDto(Author author);

    // This list mapping method will now unambiguously use authorToAuthorResponseDto
    // for its elements.
    List<AuthorResponseDto> listEntityToResponseDto(List<Author> authors);
}
