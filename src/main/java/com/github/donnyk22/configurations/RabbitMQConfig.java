package com.github.donnyk22.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_EXCHANGE = "message.exchange";

    public static final String MESSAGE_QUEUE_OBJECT = "message.queue.object";
    public static final String MESSAGE_QUEUE_TEXT = "message.queue.text";
    public static final String MESSAGE_QUEUE_ALL = "message.queue.all";

    public static final String MESSAGE_ROUTING_KEY_OBJECT = "message.key.object";
    public static final String MESSAGE_ROUTING_KEY_TEXT = "message.key.text";
    public static final String MESSAGE_ROUTING_KEY_ALL = "message.key.*"; //or "message.#"

    // =================== Job-related queues ===================
    
    public static final String JOB_EXCHANGE = "job.exchange";
    public static final String JOB_QUEUE = "job.queue";
    public static final String JOB_ROUTING_KEY = "job.key";

    // exchange is a gateway between producers and queues
    // Producer A  \
    // Producer B   --->  EXCHANGE (1) ---> Queues
    // Producer C  /
    @Bean
    public TopicExchange messageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE, true, false);
    }

    @Bean
    public Queue messageObject() {
        return QueueBuilder
            .durable(MESSAGE_QUEUE_OBJECT)
            .build();
    }

    @Bean
    public Queue messageText() {
        return QueueBuilder
            .durable(MESSAGE_QUEUE_TEXT)
            .build();
    }

    @Bean
    public Queue messageAll() {
        return QueueBuilder
            .durable(MESSAGE_QUEUE_ALL)
            .build();
    }

    // “Jika ada pesan masuk ke exchange messageExchange dengan routing key MESSAGE_ROUTING_KEY_OBJECT, maka masukkan pesan itu ke queue messageObjectQueue.”
    // Satu key bisa ke banyak queue, dan satu queue bisa terkoneksi ke banyak key (many to many)
    @Bean
    public Binding messageObjectSent(@Qualifier("messageObject") Queue messageObjectQueue, TopicExchange messageExchange) {
        return BindingBuilder
            .bind(messageObjectQueue)
            .to(messageExchange)
            .with(MESSAGE_ROUTING_KEY_OBJECT);
    }

    @Bean
    public Binding messageTextSent(@Qualifier("messageText") Queue messageTextQueue, TopicExchange messageExchange) {
        return BindingBuilder
            .bind(messageTextQueue)
            .to(messageExchange)
            .with(MESSAGE_ROUTING_KEY_TEXT);
    }

    @Bean
    public Binding messageAllSent(@Qualifier("messageAll") Queue messageAllQueue, TopicExchange messageExchange) {
        return BindingBuilder
            .bind(messageAllQueue)
            .to(messageExchange)
            .with(MESSAGE_ROUTING_KEY_ALL);
    }

    // =================== Job-related queues ===================

    @Value("${app.async.max-queue}")
    private Integer MAX_QUEUE;

    @Bean
    public Queue jobQueue() {
        Map<String, Object> args = new HashMap<>();
        // max messages stored in queue
        args.put("x-max-length", MAX_QUEUE);
        // if queue is full, drop oldest message
        // other options: reject-publish, drop-head
        args.put("x-overflow", "reject-publish");
        return new Queue(JOB_QUEUE, true, false, false, args);
    }

    @Bean
    public TopicExchange jobExchange() {
        return new TopicExchange(JOB_EXCHANGE, true, false);
    }

    @Bean
    public Binding jobSent(Queue jobQueue, TopicExchange jobExchange) {
        return BindingBuilder
            .bind(jobQueue)
            .to(jobExchange)
            .with(JOB_ROUTING_KEY);
    }

}