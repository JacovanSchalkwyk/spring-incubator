package entelect.training.incubator.spring.notification.sms.client.model;

import lombok.Data;

@Data
public class NotificationSendRequest {
    private String phoneNumber;
    private String flightNumber;
    private String nameAndSurname;
    private String flightDate;
}
