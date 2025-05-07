package com.dtb.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {

    @RabbitListener(queues = "${service.rmq.event.queue-name}")
    public void receiveSms(String message) {
        log.info("Event received: {}", message);

        //send sms
        log.info("Sending SMS...");

        //send notification
        log.info("Sending Email...");

        //send notification
        log.info("Sending notification...");
    }

}
