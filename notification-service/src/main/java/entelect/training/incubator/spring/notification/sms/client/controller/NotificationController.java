package entelect.training.incubator.spring.notification.sms.client.controller;

import entelect.training.incubator.spring.notification.sms.client.Producer;
import entelect.training.incubator.spring.notification.sms.client.model.NotificationSend;
import entelect.training.incubator.spring.notification.sms.client.model.NotificationSendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notification")
public class NotificationController {

    private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    private final Producer producer;

    public NotificationController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<?> addToQueue(@RequestBody NotificationSendRequest notification) {
        LOGGER.info("Notification received: {}", notification);
        String message = "Molo Air: Confirming flight " + notification.getFlightNumber() + " booked for "
                + notification.getNameAndSurname() + " on " + notification.getFlightDate() + ".";
        NotificationSend notificationSend = new NotificationSend();
        notificationSend.setPhoneNumber(notification.getPhoneNumber());
        notificationSend.setMessage(message);
        producer.sendMessage("inbound.queue", notificationSend);
//
        LOGGER.info("Notification sent");
        return ResponseEntity.ok("Notification sent");
    }
}