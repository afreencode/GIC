package org.gic;

interface BookingService {
    void bookSeats(int numSeats);
    void showSeatMap();
    void showBooking(String bookingId);
    void bookSeats(String seatNumber, int numSeats);
}
