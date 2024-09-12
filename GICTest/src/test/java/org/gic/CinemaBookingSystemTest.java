package org.gic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class CinemaBookingSystemTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream inputStreamCaptor = new ByteArrayInputStream("".getBytes());
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testMain_ValidInputInitialization() {
        String input = "MovieTitle 5 10\n3\n"; // Mock user input
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:"));
        assertTrue(output.contains("Welcome to GIC Cinemas"));
        assertTrue(output.contains("[1] Book tickets for MovieTitle (50 seats available)"));
        assertTrue(output.contains("[2] Check bookings"));
        assertTrue(output.contains("[3] Exit"));

    }

    @Test
    public void testMain_InvalidInputInitialization() {

        String input ="";
        String output = "";
        input = "InvalidInput\nY\nMovieTitle 5 10\n3\n"; // Mock user input
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Please enter exactly three space-separated values."));
        assertTrue(output.contains("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:"));

        input = "MovieTitle 100 100\nY\nMovieTitle 5 10\n3\n"; // Mock user input
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("Invalid input: The number of rows must be <= 26 and the number of seats per row must be <= 50 and both must be > 0."));

        input = "MovieTitle A B\nY\nMovieTitle 5 10\n3\n"; // Mock user input

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("Invalid input: Please ensure the rows and seats per row inputs are integers."));

    }

    @Test
    public void testMain_ValidBookingFlowWithDefaultSeatSelection() {
        //String input = "MovieTitle 5 10\n1\n3\n\nn\n4\n3\n";
        String input = "MovieTitle 5 10\n1\n3\n\n3";// Mock user input sequence
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Successfully reserved 3 MovieTitle tickets"));
        assertTrue(output.contains("Booking Id: GIC0001"));

    }
    @Test
    public void testMain_ValidBookingFlowWithSeatSelection() {
        //String input = "MovieTitle 5 10\n1\n3\n\nn\n4\n3\n";
        String input = "MovieTitle 5 10\n1\n3\nA03\n\n3";// Mock user input sequence
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Booking Id: GIC0001 confirmed."));

    }

    @Test
    public void testMain_ValidBookingFlowWithInvalidSeatSelection() {
        //String input = "MovieTitle 5 10\n1\n3\n\nn\n4\n3\n";
        String input = "MovieTitle 5 10\n1\n3\nA71\n\n3";// Mock user input sequence
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Please enter a valid seat number e.g. A03, B14"));

    }

    @Test
    public void testMain_SeatsNotAvailable() {
        //String input = "MovieTitle 5 10\n1\n3\n\nn\n4\n3\n";
        String input = "MovieTitle 5 10\n1\n100\n\n3";// Mock user input sequence
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Sorry, there are only 50 seats available."));

    }

    @Test
    public void testMain_ExitOption() {
        String input = "MovieTitle 5 10\n3\n"; // Mock user input for exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CinemaBookingSystem.main(null);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Thank you for using GIC Cinemas system. Bye!"));
    }

}