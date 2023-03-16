package entelect.training.incubator.spring.flight.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String flightNumber;

    private String origin;
    @Column(name = "destination")
    private String destination;
    
    private LocalDateTime departureTime;
    
    private LocalDateTime arrivalTime;
    
    private Integer seatsAvailable;
    
    private Float seatCost;

}
