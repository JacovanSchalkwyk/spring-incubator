package entelect.training.incubator.spring.notification.sms.client;

import com.google.gson.Gson;
import entelect.training.incubator.spring.notification.sms.client.model.NotificationSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final String queueName, final NotificationSend notificationSend) {
        final String textMessage = notificationSend.getMessage() + ";" + notificationSend.getPhoneNumber();
        System.out.println("Sending message " + textMessage + "to queue - " + queueName);
        jmsTemplate.send("inbound.queue", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(textMessage);
                return message;
            }
        });
    }

}