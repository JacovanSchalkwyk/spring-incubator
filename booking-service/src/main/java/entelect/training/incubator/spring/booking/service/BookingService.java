package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.BookingResponse;
import entelect.training.incubator.spring.booking.repository.BookingRepository;

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

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        if (!apiCustomerExists(bookingRequest.getCustomerId())) {
            throw new RuntimeException("Customer does not exist");
        }

        if (!apiFlightExists(bookingRequest.getFlightId())) {
            throw new RuntimeException("Flight does not exist");
        }

        Booking booking = mapBookingRequestToBooking(bookingRequest);
        booking.setReferenceNumber("ABC" + (int) (Math.random() * 9000 + 1000));
        bookingRepository.save(booking);
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

    private boolean apiCustomerExists(Integer customerId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8202/customers/" + customerId))
                .GET()
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:is_a_lie".getBytes()))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean apiFlightExists(Integer flightId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8203/flights/" + flightId))
                .GET()
                .header("Authorization", "Basic " + java.util.Base64.getEncoder()
                        .encodeToString("admin:is_a_lie".getBytes()))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Booking> getBookingsByCustomerId(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public Booking getBookingsByReference(String reference) {
        return bookingRepository.findByReferenceNumber(reference);
    }
}
