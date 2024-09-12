package org.gic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BookingHelperTest {

    @Test
    public void testValidInput() {
        assertTrue(BookingHelper.validateInput("MovieTitle 10 20"));
    }

    @Test
    public void testEmptyInput() {
        assertFalse(BookingHelper.validateInput(""), "Input cannot be empty");
    }

    @Test
    public void testMoreThanThreeValues() {
        assertFalse(BookingHelper.validateInput("MovieTitle 10 20 ExtraValue"),
                "Please enter exactly three space-separated values.");
    }

    @Test
    public void testLessThanThreeValues() {
        assertFalse(BookingHelper.validateInput("MovieTitle 10"),
                "Please enter exactly three space-separated values.");
    }

    @Test
    public void testNonIntegerRowValue() {
        assertFalse(BookingHelper.validateInput("MovieTitle A 20"),
                "Invalid input: Please ensure the rows and seats per row inputs are integers.");
    }

    @Test
    public void testNonIntegerSeatsValue() {
        assertFalse(BookingHelper.validateInput("MovieTitle 10 B"),
                "Invalid input: Please ensure the rows and seats per row inputs are integers.");
    }

    @Test
    public void testRowExceedsMax() {
        assertFalse(BookingHelper.validateInput("MovieTitle 27 20"),
                "Invalid input: The number of rows must be <= 26 and the number of seats per row must be <= 50.");
    }

    @Test
    public void testSeatsExceedsMax() {
        assertFalse(BookingHelper.validateInput("MovieTitle 10 51"),
                "Invalid input: The number of rows must be <= 26 and the number of seats per row must be <= 50.");
    }

    @Test
    public void testRowAndSeatsExceedMax() {
        assertFalse(BookingHelper.validateInput("MovieTitle 30 60"),
                "Invalid input: The number of rows must be <= 26 and the number of seats per row must be <= 50.");
    }

    @Test
    public void testValidBoundaryInput() {
        assertTrue(BookingHelper.validateInput("MovieTitle 26 50"));
    }
}