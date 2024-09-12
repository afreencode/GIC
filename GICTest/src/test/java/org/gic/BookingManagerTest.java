
package org.gic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.HashMap;
import static org.gic.BookingTestConstants.*;

public class BookingManagerTest {

    private SeatMap seatMap;
    private BookingManager bookingManager;

    @BeforeEach
    public void setUp() {
        seatMap = new SeatMap(5, 10);  // 5 rows, 10 seats per row
        bookingManager = new BookingManager("Test Movie", seatMap);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Movie", bookingManager.getMovieTitle(), "Movie title should be correctly initialized");
        assertEquals(50, bookingManager.getAvailability(), "Initial availability should be total seats");
    }

    @Test
    public void testCheckAvailability() {
        assertTrue(bookingManager.checkAvailability(5), "Should return true when enough seats are available");
        assertFalse(bookingManager.checkAvailability(100), "Should return false when not enough seats are available");
    }

    @Test
    public void testBookSeatsWithoutUserSelection() {
        bookingManager.bookSeats(10);
        char[][] seats = seatMap.getSeats();
        int bookedSeats = 0;
        for (char[] row : seats) {
            for (char seat : row) {
                if (seat == WHITECIRCLE) {  // Assuming '○' is used to mark booked seats
                    bookedSeats++;
                }
            }
        }
        assertEquals(10, bookedSeats, "10 seats should be booked");
    }

    @Test
    public void testBookSeatsWithUserSelection() {
        bookingManager.bookSeats("2-3", 5);  // Book 5 seats starting from row 2, column 3
        char[][] seats = seatMap.getSeats();
        assertEquals(WHITECIRCLE, seats[2][3], "The seat at row 2, col 3 should be booked");
    }

    @Test
    public void testUpdateBooking() {
        bookingManager.bookSeats("1-1", 5);
        bookingManager.updateBooking("C03", 3);  // Update to book 3 seats starting at 2-2
        bookingManager.confirmSeats(3);
        assertEquals(47, bookingManager.getAvailability(), "Availability should reflect the updated booking");
    }

    @Test
    public void testConfirmSeats() {
        bookingManager.bookSeats(8);
        bookingManager.displayBookingId(8);
        bookingManager.confirmSeats(8);

        assertEquals(42, bookingManager.getAvailability(), "Confirmed seats should reduce availability");

        HashMap<String, List<String>> bookingMap = bookingManager.getBookingMap();
        List<String> bookedSeats = bookingMap.get("GIC0001");
        assertNotNull(bookedSeats, "Booking map should have the confirmed booking");
        assertEquals(8, bookedSeats.size(), "Confirmed booking should have 8 seats");
    }

    @Test
    public void testShowSeatMap() {
        bookingManager.bookSeats(10);
        bookingManager.showSeatMap();  // Ensure no exception is thrown and seat map is displayed correctly
    }

    @Test
    public void testShowBooking() {
        bookingManager.bookSeats(6);
        bookingManager.displayBookingId(6);
        bookingManager.confirmSeats(6);
        bookingManager.showBooking("GIC0001");  // Display the booking for the given booking id
        List<String> bookedSeats = bookingManager.getBookingMap().get("GIC0001");
        assertNotNull(bookedSeats, "Booking should exist for GIC0001");
    }

    @Test
    public void testDisplayBookingId() {
        bookingManager.bookSeats(4);
        bookingManager.displayBookingId(4);
        bookingManager.confirmSeats(4);
        // Ensure the booking ID is updated and seats are displayed
        assertEquals("GIC0001", bookingManager.getBookingMap().keySet().iterator().next(), "Booking ID should be GIC0001");
    }
}