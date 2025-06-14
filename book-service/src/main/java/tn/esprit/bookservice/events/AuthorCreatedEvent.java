package tn.esprit.bookservice.events;

import java.io.Serializable;

public class AuthorCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private String authorId;
    private String name;
    // Add any other fields that are part of the event from author-service

    // Default constructor (needed for Jackson deserialization)
    public AuthorCreatedEvent() {
    }

    // Constructor used by the producer (optional here, but good for consistency)
    public AuthorCreatedEvent(String authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    // Getters and Setters (crucial for Jackson deserialization)
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorCreatedEvent{" +
                "authorId='" + authorId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}