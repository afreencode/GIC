package org.gic;

import java.util.Scanner;

public class CinemaBookingSystem {

    private static BookingManager bookingManager;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean initializationDone = false;
        while (!initializationDone) {

            System.out.println("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            boolean isValid = BookingHelper.validateInput(input);

            if (isValid) {
                initializeBookingManager(input);
                initializationDone = true;
            } else {
                System.out.println("Do you wish to try again? (Y/N)");
                System.out.print("> ");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "Y":
                    case "y":
                        break;
                    case "N":
                    case "n":
                        System.out.println("Exiting now");
                        System.exit(0);
                    default:
                        System.out.println("Invalid input.");
                        break;
                }
            }

        }

        while (true) {
            System.out.println("Welcome to GIC Cinemas");
            System.out.println("[1] Book tickets for " + bookingManager.getMovieTitle() + " (" + bookingManager.getAvailability() + " seats available)");
            System.out.println("[2] Check bookings");
            System.out.println("[3] Exit");

            System.out.println("Please enter your selection:");
            System.out.print("> ");

            int choice = 0;
            boolean isValidChoice = true;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer(1/2/3).");
                isValidChoice = false;
            }

            if (isValidChoice) {
                switch (choice) {
                    case 1:
                        boolean isAvailable = false;
                        String input;
                        while (!isAvailable) {
                            System.out.println("\nEnter the number of tickets to book, or enter blank to go back to main menu:");
                            System.out.print("> ");
                            input = scanner.nextLine().trim();

                            if (input.isEmpty()) {
                                isAvailable = true;

                            } else {
                                try {
                                    int numSeats = Integer.parseInt(input);
                                    if (!bookingManager.checkAvailability(numSeats)) {
                                        System.out.println("Sorry, there are only " + bookingManager.getAvailability() + " seats available.");

                                    } else {
                                        isAvailable = true;
                                        bookingManager.bookSeats(numSeats);
                                        bookingManager.displayBookingId(numSeats);
                                        boolean isFinal = false;
                                        while (!isFinal) {
                                            System.out.println("Enter blank to accept seat selection, or enter new seat position");
                                            System.out.print("> ");
                                            String seatNumber = scanner.nextLine().trim();
                                            if (seatNumber.isEmpty()) {
                                                bookingManager.confirmSeats(numSeats);
                                                isFinal = true;
                                            } else {
                                                if(bookingManager.validateSeatNumber(seatNumber)) {
                                                    bookingManager.updateBooking(seatNumber, numSeats);
                                                }else{
                                                    System.out.println("Please enter a valid seat number e.g. A03, B14");
                                                }
                                            }
                                        }
                                    }

                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid integer.");
                                }
                            }
                        }
                        break;
                    case 2:
                        boolean toDisplay = false;
                        while (!toDisplay) {
                            System.out.println("\nEnter Booking Id, or enter blank to go back to main menu:");
                            System.out.print("> ");
                            String bookingId = scanner.nextLine().trim();
                            if (bookingId.isEmpty()) {
                                toDisplay = true;
                            } else {
                                bookingManager.showBooking(bookingId);
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Thank you for using GIC Cinemas system. Bye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void initializeBookingManager(String input) {

        String[] inputs = input.split(" ");

        String movieTitle = inputs[0];
        int row = Integer.parseInt(inputs[1]);
        int seatsPerRow = Integer.parseInt(inputs[2]);

        SeatMap seatMap = new SeatMap(row, seatsPerRow);
        bookingManager = new BookingManager(movieTitle, seatMap);

        System.out.println();
    }


}
