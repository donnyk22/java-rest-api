package com.github.donnyk22.services.msbroker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.github.donnyk22.configurations.RabbitMQConfig;
import com.github.donnyk22.models.forms.MsBrokerForm;
import com.github.donnyk22.utils.ConverterUtil;

@Service
public class MsBrokerServiceImpl implements MsBrokerService {

    private static final Logger logger = LoggerFactory.getLogger(MsBrokerServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public MsBrokerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public MsBrokerForm sendToTopicObject(MsBrokerForm object) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.MESSAGE_EXCHANGE,
            RabbitMQConfig.MESSAGE_ROUTING_KEY_OBJECT,
            ConverterUtil.objectToBytes(object)
        );
        return object;
    }

    @Override
    public String sendToTopicText(String text) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.MESSAGE_EXCHANGE,
            RabbitMQConfig.MESSAGE_ROUTING_KEY_TEXT,
            text
        );
        return text;
    }

    //Listeners

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE_OBJECT)
    private void object(byte[] object) {
        logger.info("Received message object topic: {}", ConverterUtil.bytesToString(object));
    }

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE_TEXT)
    private void text(String text) {
        logger.info("Received message text topic: {}", text);
    }

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE_ALL)
    public void all(Message message) {
        byte[] body = message.getBody();
        MessageProperties props = message.getMessageProperties();

        String str = ConverterUtil.bytesToString(body);

        if ("application/octet-stream".equals(props.getContentType())) {
            logger.info("Received message object via all topic listener: {}", str);
            return;
        }

        if ("text/plain".equals(props.getContentType())) {
            logger.info("Received message text via all topic listener: {}", str);
        }
    }
}