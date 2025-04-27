package tn.esprit.bookservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthorResponseDto {
    private String id;
    private String name;
    private String email;
    private String bio;
}
