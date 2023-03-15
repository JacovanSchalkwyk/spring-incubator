package entelect.training.incubator.spring.notification.sms.client.model;

import lombok.Data;

@Data
public class NotificationSend {
    private String phoneNumber;
    private String message;
}
