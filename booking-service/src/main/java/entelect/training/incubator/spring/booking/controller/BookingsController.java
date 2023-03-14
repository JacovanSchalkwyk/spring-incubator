package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.*;
import entelect.training.incubator.spring.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingService bookingsService;

    public BookingsController(BookingService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest booking) {
        LOGGER.info("Processing booking creation request for booking={}", booking);

        try {
            final BookingResponse savedbooking = bookingsService.createBooking(booking);
            LOGGER.info("Booking created");
            return new ResponseEntity<>(savedbooking, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error creating booking");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Integer id) {
        LOGGER.info("Processing get booking by id request for id={}", id);

        final BookingResponse booking = bookingsService.getBookingById(id);
        if (booking == null) {
            LOGGER.error("Booking not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("Booking found");
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping("search/customerId")
    public ResponseEntity<List<Booking>> getBookingsByCustomerId(@RequestBody BookingsSearchByCustomerRequest customer) {
        LOGGER.info("Processing get bookings by customer id request for customerId={}", customer.getCustomerId());
        final List<Booking> bookings = bookingsService.getBookingsByCustomerId(customer.getCustomerId());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping("search/reference")
    public ResponseEntity<Booking> getBookingsByReferenceId(@RequestBody BookingsSearchByReferenceRequest customer) {
        LOGGER.info("Processing get bookings by reference id request for referenceId={}", customer.getReferenceNumber());
        final Booking bookings = bookingsService.getBookingsByReference(customer.getReferenceNumber());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}