package tn.esprit.bookservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.bookservice.dto.AuthorResponseDto; // Import the DTO

import java.util.List;

@FeignClient(name = "author-service-client", url = "${author.service.url}", path = "/api/authors")
public interface AuthorClient {

    /**
     * Fetches an author by their ID from the author-service.
     * Corresponds to the GET /authors/{id} endpoint in AuthorController.
     *
     * @param id The ID of the author to fetch.
     * @return AuthorResponseDto containing the author details.
     */
    @GetMapping("/{id}")
    AuthorResponseDto getAuthorById(@PathVariable("id") String id);


    // Example: If author-service had an endpoint to get all authors
    @GetMapping
    List<AuthorResponseDto> getAllAuthors();


    // Example: If author-service had an endpoint to check if an author exists
    @GetMapping("/exists/{id}")
    boolean checkAuthorExists(@PathVariable("id") String id);

}