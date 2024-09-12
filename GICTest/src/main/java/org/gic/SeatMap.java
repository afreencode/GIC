package org.gic;

import java.util.ArrayList;
import java.util.List;
import static org.gic.BookingConstants.*;

public class SeatMap {
    private static int ROWS;
    private static int COLS;
    private final char[][] seats;
    private final List<String> seatNumbers;

    public SeatMap(int rows, int cols) {
        ROWS = rows;
        COLS = cols;
        seats = new char[ROWS][COLS];
        seatNumbers = generateSeatNumbers(rows, cols);
        initializeSeats();
    }

    // Initialize seats array as per user inputs
    private void initializeSeats() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seats[i][j] = BULLET;
            }
        }
    }

    public char[][] getSeats() {
        return seats;
    }

    public void showSeatMap() {
        String screenLabel = "Screen";
        int totalWidth = COLS * 3 + 3;
        int padding = (totalWidth - screenLabel.length()) / 2;

        System.out.println(" ".repeat(padding) + screenLabel + " ".repeat(padding));

        System.out.print("   ");
        for (int i = 1; i <= COLS; i++) {
            System.out.print(" - ");
        }
        System.out.println();

        for (int i = ROWS - 1; i >= 0; i--) {
            System.out.printf("%2c ", 'A' + i);
            for (int j = 0; j < COLS; j++) {
                System.out.printf("%2s ", seats[i][j]);
            }
            System.out.println();
        }

        System.out.print("   ");
        for (int i = 1; i <= COLS; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println("\n");
    }

    /* Return the number of seats available in a given row
       In case of default selection, return empty seats in entire row
       In case the seat position is provided by user, return the number
       of empty seats available to the right of given position
     */
    public int getSeatsToBookInRow(int rowIndex, int startCol, int numSeats, boolean isUserSelected) {

        int seatsAvailable = 0;
        if(isUserSelected){
            for (int j=startCol;j<COLS;j++){
                if(seats[rowIndex][j] == BULLET){
                    seatsAvailable++;
                }
            }

            if (numSeats <= seatsAvailable) {
                return numSeats;
            } else {
                return seatsAvailable;
            }
        }

        for (int j=0;j<COLS;j++){

            if(seats[rowIndex][j] == BULLET){
                seatsAvailable++;
            }
        }

        if (numSeats <= seatsAvailable) {
            return numSeats;
        } else {
            return seatsAvailable;
        }

    }

    /* Book the seats in a given row
       In case of default selection, start booking from middle
       In case the seat position is provided by user, start with given position
     */
    public void bookSeatsInRow(int rowIndex, int startCol, int numSeats, boolean isUserSelected) {

        int startColIndex = 0;
        int seatsAvailable = 0;

        if (isUserSelected) {
            startColIndex = startCol;
        }else{
            for (int j = 0; j < COLS; j++) {

                if (seats[rowIndex][j] == BULLET) {
                    seatsAvailable++;
                }
            }

            if (seatsAvailable != COLS) {
                startColIndex = COLS / 2;
            } else {
                startColIndex = COLS / 2 - numSeats / 2;
            }

        }

        int seatsBooked = 0;
        for (int j = startColIndex; j < COLS && seatsBooked < numSeats; j++) {
            if (seats[rowIndex][j] == BULLET) {

                seats[rowIndex][j] = WHITECIRCLE; // Mark as currently booked seat
                seatsBooked++;
            }
        }

        int leftoverSeats = numSeats - seatsBooked;

        if(leftoverSeats > 0) {
            startColIndex = COLS / 2;
            for (int j = startColIndex; j >=0 && leftoverSeats !=0; j--) {

                if (seats[rowIndex][j] == BULLET) {

                    seats[rowIndex][j] = WHITECIRCLE; // Mark as currently booked seat
                    leftoverSeats--;
                }
            }
        }

    }

    //After booking is confirmed, mark the booked seats with #
    public void updateBookedSeatsToHash() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                if (seats[i][j] == WHITECIRCLE) {
                    seats[i][j] = '#';
                }
            }
        }
    }

    // Mark the booked seats as empty in the current booking after user requests seat update
    public void reverseBooking() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                if (seats[i][j] == WHITECIRCLE) {
                    seats[i][j] = BULLET;
                }
            }
        }
    }

    /*
     Parse the seat number provided by user and map to array index.
     e.g. B05 is mapped to 1-4, row 1 and col 4
     */
    public  String mapSeatNumberToArray(String seatNumber){

        int row = seatNumber.charAt(0) - 'A';
        int column = Integer.parseInt(seatNumber.substring(1))-1;

        return row + "-" + column;
    }

    private  List<String> generateSeatNumbers(int rows, int seatsPerRow) {
        List<String> seatNumbers = new ArrayList<>();

        // Loop through the specified number of rows and seats
        for (int row = 0; row < rows; row++) {
            char rowLetter = (char) ('A' + row); // Convert 0-based index to row letter (A-Z)

            for (int seat = 1; seat <= seatsPerRow; seat++) {
                // Format seat number as two digits (e.g., 01, 02, ..., 50)
                String formattedSeatNumber = String.format("%02d", seat);

                // Combine row letter and seat number (e.g., A01, A02, ..., Z50)
                seatNumbers.add(rowLetter + formattedSeatNumber);
            }
        }

        return seatNumbers;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }
}
