package tn.esprit.authorservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "author_events_exchange";
    public static final String AUTHOR_CREATED_QUEUE_NAME = "author_created_queue";
    public static final String AUTHOR_CREATED_ROUTING_KEY = "author.created";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue authorCreatedQueue() {
        // durable: false for simplicity in example, true for production
        return new Queue(AUTHOR_CREATED_QUEUE_NAME, false);
    }

    @Bean
    Binding authorCreatedBinding(Queue authorCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(authorCreatedQueue).to(exchange).with(AUTHOR_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
}