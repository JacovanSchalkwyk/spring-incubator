package entelect.training.incubator.spring.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class Flight {
    
    private Integer id;
    
    private String flightNumber;

    private String origin;

    private String destination;
    
    private String departureTime;
    
    private String arrivalTime;
    
    private Integer seatsAvailable;
    
    private Float seatCost;

}
