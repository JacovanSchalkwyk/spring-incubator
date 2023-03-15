package entelect.training.incubator.spring.notification.sms.client;

import com.google.gson.Gson;
import entelect.training.incubator.spring.notification.sms.client.impl.MoloCellSmsClient;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class Listener {
    @Autowired
    private MoloCellSmsClient moloCellSmsClient;

    @JmsListener(destination = "inbound.queue")
    @SendTo("outbound.queue")
    public String receiveMessage(final Message jsonMessage) throws JMSException {
        String messageData = null;
        String response = null;
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            response = messageData;
        }
        String[] info = messageData.split(";");
        moloCellSmsClient.sendSms(info[1], info[0]);
        return response;
    }

    @JmsListener(destination = "inbound.topic")
    @SendTo("outbound.topic")
    public String receiveMessageFromTopic(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        String response = null;
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            Map map = new Gson().fromJson(messageData, Map.class);
            response  = "Hello " + map.get("name");
        }
        return response;
    }

}