package entelect.training.incubator.spring.booking.model;

import lombok.Data;

@Data
public class NotificationSendRequest {
    private String phoneNumber;
    private String flightNumber;
    private String nameAndSurname;
    private String flightDate;
}
