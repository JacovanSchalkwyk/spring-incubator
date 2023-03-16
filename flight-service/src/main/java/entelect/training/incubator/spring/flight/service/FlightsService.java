package entelect.training.incubator.spring.flight.service;

import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.model.FlightsSearchRequest;
import entelect.training.incubator.spring.flight.model.SearchType;
import entelect.training.incubator.spring.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class FlightsService {

    @Value("${loyalty.discount.flights.selection.futureDays}")
    private int discountedFlightFutureDays;

    @Value("${loyalty.discount.flights.limit}")
    private int discountedFlightLimit;

    @Value("${loyalty.discount.fares.rate}")
    private float discountedRate;

    private final FlightRepository flightRepository;

    public FlightsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
        Flight flight = new Flight();
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flight.setOrigin("JNB");
        flight.setDestination("CPT");
        flight.setSeatsAvailable(4);
        flight.setSeatCost(1000.00f);
        flightRepository.save(flight);
        flight = new Flight();
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flight.setOrigin("CPT");
        flight.setDestination("JNB");
        flight.setSeatsAvailable(5);
        flight.setSeatCost(1000.00f);
        flightRepository.save(flight);
    }

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> getFlights() {
        Iterable<Flight> flightIterable = flightRepository.findAll();

        List<Flight> result = new ArrayList<>();
        flightIterable.forEach(result::add);

        return result;
    }

    public Flight getFlight(Integer id) {
        return flightRepository.findById(id).orElse(null);
    }

    public List<Flight> getDiscountedFlights() {
        final List<Flight> futureFlights = flightRepository.findByDepartureTimeBetweenDates(LocalDateTime.now(), LocalDateTime.now().plusDays(discountedFlightFutureDays));

        // pick random flights
        return futureFlights.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(discountedFlightLimit)
                .map(flight -> {
                    flight.setSeatCost(flight.getSeatCost() * discountedRate);
                    return flight;
                })
                .collect(Collectors.toList());
    }
//    @Cacheable("flights")
//    @CachePut(value="flights")
    public List<Flight> searchFlights(FlightsSearchRequest searchRequest) {
        Map<SearchType, Supplier<List<Flight>>> searchStrategies = new HashMap<>();

        searchStrategies.put(SearchType.DAYS_TO_DEPARTURE_SEARCH, () -> flightRepository.findByDepartureTimeBetweenDates(
                LocalDateTime.now(), LocalDateTime.now().plusDays(searchRequest.getDaysToDeparture()))
        );
        searchStrategies.put(SearchType.DEPARTURE_TIME_SEARCH, () -> flightRepository.findByDepartureTimeBetween(
                searchRequest.getDepartureDateFrom(),
                searchRequest.getDepartureDateTo())
        );
        searchStrategies.put(SearchType.ORIGIN_DESTINATION_SEARCH, () -> flightRepository.findByOriginAndDestination(
                searchRequest.getOrigin(),
                searchRequest.getDestination())
        );
        return searchStrategies.get(searchRequest.getSearchType()).get();
    }

    public List<String> getUniqueDestinations() {
        return flightRepository.findDistinctDestination();
    }

    public List<String> getUniqueOrigins() {
        return flightRepository.findDistinctOrigin();
    }
}
