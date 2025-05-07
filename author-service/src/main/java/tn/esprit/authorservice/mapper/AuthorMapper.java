package tn.esprit.authorservice.mapper;
import org.mapstruct.Mapper;
import tn.esprit.authorservice.dto.AuthorRequestDto;
import tn.esprit.authorservice.dto.AuthorResponseDto;
import tn.esprit.authorservice.entities.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    // Maps Author entity to AuthorResponseDto
    AuthorResponseDto entityToResponseDto(Author author);

    Author requestDtoToEntity(AuthorRequestDto authorRequestDto);

    List<AuthorResponseDto> listEntityToResponseDto(List<Author> authors);
}
