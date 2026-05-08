package com.trainticket.repository;

import com.trainticket.model.Booking;
import java.util.List;

public class BookingRepository {
    private static final JsonRepository<Booking> repo = new JsonRepository<>("data/bookings.json", Booking[].class);

    public static List<Booking> findAll() {
        return repo.findAll();
    }

    public static void saveAll(List<Booking> bookings) {
        repo.saveAll(bookings);
    }
}
