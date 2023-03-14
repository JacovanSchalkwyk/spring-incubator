package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.BookingResponse;
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

//    @GetMapping
//    public ResponseEntity<?> getCustomers() {
//        LOGGER.info("Fetching all customers");
//        List<Booking> customers = customersService.getCustomers();
//
//        if (!customers.isEmpty()) {
//            LOGGER.trace("Found customers");
//            return new ResponseEntity<>(customers, HttpStatus.OK);
//        }
//
//        LOGGER.info("No customers could be found");
//        return ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
//        LOGGER.info("Processing customer search request for customer id={}", id);
//        Booking customer = this.customersService.getCustomer(id);
//
//        if (customer != null) {
//            LOGGER.trace("Found customer");
//            return new ResponseEntity<>(customer, HttpStatus.OK);
//        }
//
//        LOGGER.trace("Customer not found");
//        return ResponseEntity.notFound().build();
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity<?> searchCustomers(@RequestBody CustomerSearchRequest searchRequest) {
//        LOGGER.info("Processing customer search request for request {}", searchRequest);
//
//        Booking customer = customersService.searchCustomers(searchRequest);
//
//        if (customer != null) {
//            return ResponseEntity.ok(customer);
//        }
//
//        LOGGER.trace("Customer not found");
//        return ResponseEntity.notFound().build();
//    }
}