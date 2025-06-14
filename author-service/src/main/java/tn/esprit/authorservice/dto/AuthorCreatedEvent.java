package tn.esprit.authorservice.dto;

import java.io.Serializable;

public class AuthorCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String authorId;
    private String name;

    public AuthorCreatedEvent() {}

    public AuthorCreatedEvent(String authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    // Getters and Setters
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "AuthorCreatedEvent{" + "authorId='" + authorId + '\'' + ", name='" + name + '\'' + '}';
    }
}