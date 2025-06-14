package tn.esprit.authorservice.services;

import lombok.RequiredArgsConstructor; // Changed from @AllArgsConstructor
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import tn.esprit.authorservice.config.RabbitMQConfig;
import tn.esprit.authorservice.dto.AuthorCreatedEvent;
import tn.esprit.authorservice.dto.AuthorRequestDto;
import tn.esprit.authorservice.dto.AuthorResponseDto;
import tn.esprit.authorservice.entities.Author;
import tn.esprit.authorservice.mapper.AuthorMapper;
import tn.esprit.authorservice.repository.AuthorRepository;
import tn.esprit.authorservice.services.interfaces.IAuthorService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final RabbitTemplate rabbitTemplate;

    @Value("${welcome.message:Default Welcome}")
    private String welcomeMessage;

    @Override
    public String getWelcomeMessage() {
        log.info("Current welcome message from AuthorService: {}", welcomeMessage);
        return welcomeMessage;
    }

    @Override
    public List<AuthorResponseDto> getAllAuthors() {
        log.info("Fetching all authors. Message: {}", welcomeMessage);
        List<Author> authors = authorRepository.findAll();
        return authorMapper.listEntityToResponseDto(authors);
    }

    @Override
    public Optional<AuthorResponseDto> getAuthorById(String id) {
        log.info("Fetching author by id: {}. Message: {}", id, welcomeMessage);
        Optional<Author> authorOptional = authorRepository.findById(id);
        return authorOptional.map(authorMapper::authorToAuthorResponseDto);
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = authorMapper.authorRequestDtoToAuthor(authorRequestDto);
        Author savedAuthor = authorRepository.save(author);

        AuthorCreatedEvent event = new AuthorCreatedEvent(savedAuthor.getId(), savedAuthor.getName());
        log.info("Sending AuthorCreatedEvent: {}", event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.AUTHOR_CREATED_ROUTING_KEY, event);

        return authorMapper.authorToAuthorResponseDto(savedAuthor);
    }

    @Override
    public Optional<AuthorResponseDto> updateAuthor(String id, AuthorRequestDto authorRequestDto) {
        log.info("Updating author id: {}. Message: {}", id, welcomeMessage);
        Optional<Author> existingAuthorOptional = authorRepository.findById(id);
        if (existingAuthorOptional.isPresent()) {
            Author existingAuthor = existingAuthorOptional.get();
            existingAuthor.setName(authorRequestDto.getName());
            existingAuthor.setEmail(authorRequestDto.getEmail());
            existingAuthor.setBio(authorRequestDto.getBio());

            Author updatedAuthor = authorRepository.save(existingAuthor);
            return Optional.of(authorMapper.authorToAuthorResponseDto(updatedAuthor));
        } else {
            log.warn("Author with id {} not found for update.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteAuthor(String id) {
        log.info("Deleting author id: {}. Message: {}", id, welcomeMessage);
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        } else {
            log.warn("Author with id {} not found for deletion.", id);
            return false;
        }
    }

    public Optional<Author> findAuthorEntityById(String id) {
        log.info("Finding author entity by id: {}", id);
        return authorRepository.findById(id);
    }
}