
package org.gic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.gic.BookingTestConstants.*;

public class SeatMapTest {

    private SeatMap seatMap;

    @BeforeEach
    public void setUp() {
        seatMap = new SeatMap(5, 10);  // Initialize with 5 rows and 10 seats per row
    }

    @Test
    public void testConstructorAndInitializeSeats() {
        char[][] seats = seatMap.getSeats();
        for (char[] row : seats) {
            for (char seat : row) {
                assertEquals(BULLET, seat, "All seats should be initialized as available (" + BULLET + ")");
            }
        }
    }

    @Test
    public void testShowSeatMap() {
        // This is just a visual test, ensuring no exceptions occur
        seatMap.showSeatMap();
    }

    @Test
    public void testGetSeatsToBookInRowWithUserSelection() {
        int seatsToBook = seatMap.getSeatsToBookInRow(2, 3, 4, true);
        assertEquals(4, seatsToBook, "Should return the correct number of seats available for user-selected booking.");

        seatMap.bookSeatsInRow(2, 3, 4, true);  // Book 4 seats
        seatsToBook = seatMap.getSeatsToBookInRow(2, 3, 4, true);
        assertEquals(3, seatsToBook, "Should return 0 when no seats are available to the right.");
    }

    @Test
    public void testGetSeatsToBookInRowWithoutUserSelection() {
        int seatsToBook = seatMap.getSeatsToBookInRow(2, 0, 10, false);
        assertEquals(10, seatsToBook, "Should return all available seats when no specific seat is selected.");

        seatMap.bookSeatsInRow(2, 0, 6, false);  // Book 6 seats
        seatsToBook = seatMap.getSeatsToBookInRow(2, 0, 10, false);
        assertEquals(4, seatsToBook, "Should return the number of remaining seats in the row.");
    }

    @Test
    public void testBookSeatsInRowWithUserSelection() {
        seatMap.bookSeatsInRow(1, 4, 3, true);  // Book 3 seats starting from column 4

        char[][] seats = seatMap.getSeats();
        assertEquals(WHITECIRCLE, seats[1][4], "The seat should be booked.");
        assertEquals(WHITECIRCLE, seats[1][5], "The seat should be booked.");
        assertEquals(WHITECIRCLE, seats[1][6], "The seat should be booked.");
    }

    @Test
    public void testBookSeatsInRowWithoutUserSelection() {
        seatMap.bookSeatsInRow(1, 0, 5, false);  // Book 5 seats in the middle

        char[][] seats = seatMap.getSeats();
        assertEquals(WHITECIRCLE, seats[1][5], "The middle seat should be booked.");
        assertEquals(WHITECIRCLE, seats[1][6], "The seat next to the middle should be booked.");
        assertEquals(WHITECIRCLE, seats[1][7], "The seat next to the middle should be booked.");
    }

    @Test
    public void testUpdateBookedSeatsToHash() {
        seatMap.bookSeatsInRow(2, 0, 4, true);
        seatMap.updateBookedSeatsToHash();

        char[][] seats = seatMap.getSeats();
        for (int i = 0; i < 4; i++) {
            assertEquals('#', seats[2][i], "Seats should be marked as confirmed booked ('#').");
        }
    }

    @Test
    public void testReverseBooking() {
        seatMap.bookSeatsInRow(2, 0, 4, true);
        seatMap.reverseBooking();

        char[][] seats = seatMap.getSeats();
        for (int i = 0; i < 4; i++) {
            assertEquals(BULLET, seats[2][i], "Seats should be reset to available ("+ BULLET + ")");
        }
    }

    @Test
    public void testMapSeatNumberToArray() {
        String mappedSeat = seatMap.mapSeatNumberToArray("B05");
        assertEquals("1-4", mappedSeat, "Seat B05 should map to row 1 and column 4.");
    }

}
