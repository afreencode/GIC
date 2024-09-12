package org.gic;

import static org.gic.BookingConstants.*;

public class BookingHelper {

    public static boolean validateInput(String input) {

        if (input.isEmpty()) {
            System.out.println("Input cannot be empty");
            return false;
        }

        String[] inputs = input.split(" ");

        if (inputs.length == 3) {
            String movieTitle = inputs[0];
            String row = inputs[1];
            String seatsPerRow = inputs[2];

            try {
                if (validateNumber(Integer.parseInt(row), Integer.parseInt(seatsPerRow))) {
                    return true;
                } else {
                    System.out.println("Invalid input: The number of rows must be <= 26 and the number of seats per row must be <= 50 and both must be > 0.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please ensure the rows and seats per row inputs are integers.");
                return false;

            }


        } else {
            System.out.println("Please enter exactly three space-separated values.");
            return false;
        }

    }

    public static boolean validateNumber(int row, int seatsPerRow) {
        return row > MIN_COLS && seatsPerRow > MIN_COLS && row <= MAX_ROWS && seatsPerRow <= MAX_COLS;
    }
}
