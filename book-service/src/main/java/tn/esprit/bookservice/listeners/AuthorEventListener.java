package tn.esprit.bookservice.listeners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tn.esprit.bookservice.events.AuthorCreatedEvent;

@Component
public class AuthorEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthorEventListener.class);

    // Ensure this queue name matches the one declared in the producer's RabbitMQConfig
    // or configure it via properties.
    public static final String AUTHOR_CREATED_QUEUE_NAME = "author_created_queue";


    @RabbitListener(queues = AUTHOR_CREATED_QUEUE_NAME)
    public void handleAuthorCreatedEvent(AuthorCreatedEvent event) {
        log.info("Received AuthorCreatedEvent: {}", event);
    }
}