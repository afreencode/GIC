package org.gic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.gic.BookingConstants.*;

public class BookingManager implements BookingService {
    private final SeatMap seatMap;
    private String bookingId = "GIC0000";
    private int availability;
    private final HashMap<String, List<String>> bookingMap;
    private final String movieTitle;

    public BookingManager(String movieTitle, SeatMap seatMap) {
        this.seatMap = seatMap;
        this.movieTitle = movieTitle;
        this.availability = seatMap.getSeats().length * seatMap.getSeats()[0].length;
        this.bookingMap = new HashMap<>();
    }

    // Update current booking after user updates seat selection
    public void updateBooking(String seatNumber, int numSeats) {
        seatMap.reverseBooking();
        String index = seatMap.mapSeatNumberToArray(seatNumber);
        bookSeats(index, numSeats);
        displayBookingIdAfterUpdate();
    }

    /*
    When seating position is provided,
    1. Start from the specified position, fill up all the empty seats in the same row
       all the way to right of cinema hall
    2. When there are not enough seats available, it should overflow to the next
       row closer to the screen.
    3. Seat allocation for the overflow, follows the rule for default selection

     */
    @Override
    public void bookSeats(String seatNumber, int numSeats) {

        int selectedRow = Integer.parseInt(seatNumber.split("-")[0]);
        ;
        int selectedCol = Integer.parseInt(seatNumber.split("-")[1]);

        boolean isUserSelected = true;

        boolean booked = false;

        int startRow = selectedRow;
        int startCol = selectedCol;

        int remainingSeats = numSeats;

        while (startRow < seatMap.getSeats().length) {

            if (remainingSeats > 0) {
                int seatsToBookInCurrentRow = seatMap.getSeatsToBookInRow(startRow, startCol, remainingSeats, isUserSelected);
                if (seatsToBookInCurrentRow > 0) {
                    seatMap.bookSeatsInRow(startRow, startCol, seatsToBookInCurrentRow, isUserSelected);
                    remainingSeats = remainingSeats - seatsToBookInCurrentRow;

                    if (remainingSeats == 0) {
                        booked = true;
                        break;
                    }

                }
                isUserSelected = false;
            }
            startRow++;

        }
        if (!booked) {

            while (selectedRow >= 0) {

                if (remainingSeats > 0) {
                    int seatsToBookInCurrentRow = seatMap.getSeatsToBookInRow(selectedRow, startCol, remainingSeats, isUserSelected);
                    if (seatsToBookInCurrentRow > 0) {
                        seatMap.bookSeatsInRow(selectedRow, startCol, seatsToBookInCurrentRow, isUserSelected);
                        remainingSeats = remainingSeats - seatsToBookInCurrentRow;

                        if (remainingSeats == 0) {
                            break;
                        }
                    }
                }
                selectedRow--;
            }
        }
    }

    /*
    Default seat selection
    1. Start from furthest row from the screen
    2. Start from the middle-most possible col
    3. When a row is not enough to accommodate the number of tickets,
       overflow to next row closer to screen
    */
    @Override
    public void bookSeats(int numSeats) {

        boolean booked = false;
        int startRow = 0;
        int startCol = 0;

        int remainingSeats = numSeats;

        while (startRow < seatMap.getSeats().length) {
            // Check if there are enough seats in the current row starting from the current position
            if (remainingSeats > 0) {
                int seatsToBookInCurrentRow = seatMap.getSeatsToBookInRow(startRow, startCol, remainingSeats, false);
                if (seatsToBookInCurrentRow > 0) {
                    seatMap.bookSeatsInRow(startRow, startCol, seatsToBookInCurrentRow, false);
                    remainingSeats = remainingSeats - seatsToBookInCurrentRow;

                    if (remainingSeats == 0) {
                        booked = true;
                        break;
                    }
                }
            }
            startRow++;
        }
        if (!booked) {
            System.out.println("Seats can not be booked.");
        }
    }

    @Override
    public void showSeatMap() {
        seatMap.showSeatMap();
    }

    @Override
    public void showBooking(String bookingId) {

        System.out.println("\nBooking Id: " + bookingId);
        System.out.println("Selected seats:\n");

        List<String> coordinates = bookingMap.get(bookingId);
        if (coordinates == null) {
            System.out.println("No booking found");
        } else {
            for (String pair : coordinates) {
                String[] arr = pair.split("-");
                int i = Integer.parseInt(arr[0]);
                int j = Integer.parseInt(arr[1]);
                seatMap.getSeats()[i][j] = WHITECIRCLE;
            }
            seatMap.showSeatMap();
            for (String pair : coordinates) {
                String[] arr = pair.split("-");
                int i = Integer.parseInt(arr[0]);
                int j = Integer.parseInt(arr[1]);
                seatMap.getSeats()[i][j] = '#';
            }
        }
    }

    // Generate booking id for new booking
    private String updateBookingId(String bookingId) {
        String prefix = bookingId.substring(0, 3);
        int numericPart = Integer.parseInt(bookingId.substring(3)) + 1;
        return prefix + String.format("%04d", numericPart);
    }

    // Store seat allocation for each booking
    private void updateBookingMap(String bookingId) {
        List<String> coordinates = new ArrayList<>();
        for (int i = 0; i < seatMap.getSeats().length; i++) {
            for (int j = 0; j < seatMap.getSeats()[0].length; j++) {
                if (seatMap.getSeats()[i][j] == WHITECIRCLE) {
                    coordinates.add(i + "-" + j);
                }
            }
        }
        bookingMap.put(bookingId, coordinates);
    }

    // Actions post booking confirmation,
    public void confirmSeats(int numSeats) {

        updateBookingMap(bookingId);
        seatMap.updateBookedSeatsToHash();
        availability -= numSeats;
        System.out.println("Booking Id: " + bookingId + " confirmed.\n");

    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getAvailability() {
        return availability;
    }

    //Display booking information
    public void displayBookingId(int numSeats) {
        bookingId = updateBookingId(bookingId);
        System.out.println("\nSuccessfully reserved " + numSeats + " " + movieTitle + " tickets");
        System.out.println("Booking Id: " + bookingId);
        System.out.println("Selected seats:\n");
        showSeatMap();
    }

    // Check if enough seats are available for booking
    public boolean checkAvailability(int numSeats) {

        return numSeats <= availability;
    }

    // Display updated booking information
    public void displayBookingIdAfterUpdate() {
        System.out.println("\nBooking Id: " + bookingId);
        System.out.println("Selected seats:\n");
        showSeatMap();
    }

    // Return booking map
    public HashMap<String, List<String>> getBookingMap() {
        return bookingMap;
    }

    public boolean validateSeatNumber(String seatNumber){
        return seatMap.getSeatNumbers().contains(seatNumber);
    }

}
