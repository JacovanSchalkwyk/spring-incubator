package entelect.training.incubator.spring.booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.booking.model.*;
import entelect.training.incubator.spring.booking.repository.BookingRepository;

import java.awt.print.Book;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingResponse createBooking(BookingRequest bookingRequest) throws JsonProcessingException {
        Customer customer = apiGetCustomer(bookingRequest.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer does not exist");
        }

        Flight flight = apiFlightExists(bookingRequest.getFlightId());
        if (flight == null) {
            throw new RuntimeException("Flight does not exist");
        }

        Booking booking = mapBookingRequestToBooking(bookingRequest);
        booking.setReferenceNumber("ABC" + (int) (Math.random() * 9000 + 1000));
        bookingRepository.save(booking);
        // call notification service to send SMS
        apiSendNotification(booking, customer, flight);

        return mapBookingToBookingResponse(booking);
    }


    public BookingResponse getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            return mapBookingToBookingResponse(booking);
        }
        return null;
    }

    private BookingResponse mapBookingToBookingResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlightId(booking.getFlightId());
        bookingResponse.setCustomerId(booking.getCustomerId());
        bookingResponse.setReferenceNumber(booking.getReferenceNumber());
        return bookingResponse;
    }

    private Booking mapBookingRequestToBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setFlightId(bookingRequest.getFlightId());
        booking.setCustomerId(bookingRequest.getCustomerId());
        return booking;
    }

    private Customer apiGetCustomer(Integer customerId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8202/customers/" + customerId))
                .GET()
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:is_a_lie".getBytes()))
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper(); // or use any other JSON library
                Customer customer = objectMapper.readValue(response.body(), Customer.class);
                return customer;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Flight apiFlightExists(Integer flightId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8203/flights/" + flightId))
                .GET()
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:is_a_lie".getBytes()))
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper(); // or use any other JSON library
                Flight flight = objectMapper.readValue(response.body(), Flight.class);
                return flight;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Booking> getBookingsByCustomerId(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public Booking getBookingsByReference(String reference) {
        return bookingRepository.findByReferenceNumber(reference);
    }

    public void apiSendNotification(Booking booking, Customer customer, Flight flight) throws JsonProcessingException {
        NotificationSendRequest notificationSendRequest = new NotificationSendRequest();
        notificationSendRequest.setFlightDate(flight.getDepartureTime().toString());
        notificationSendRequest.setFlightNumber(flight.getFlightNumber());
        notificationSendRequest.setPhoneNumber(customer.getPhoneNumber());
        notificationSendRequest.setNameAndSurname(customer.getFirstName() + " " + customer.getLastName());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8206/notification"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(notificationSendRequest)))
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:is_a_lie".getBytes()))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Notification sent");
            } else {
                System.out.println("Notification failed: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
