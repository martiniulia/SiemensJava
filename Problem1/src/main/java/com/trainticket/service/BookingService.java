package com.trainticket.service;

import com.trainticket.model.Booking;
import com.trainticket.model.Train;
import com.trainticket.repository.BookingRepository;
import com.trainticket.repository.TrainRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {

    public static void bookTicket(String name, String email, String trainId, String fromStationId, String toStationId, int numSeats) {
        if (!RouteSearchService.stationExists(fromStationId)) {
            System.out.println("Error: Origin Station ID not found.");
            return;
        }
        if (!RouteSearchService.stationExists(toStationId)) {
            System.out.println("Error: Destination Station ID not found.");
            return;
        }

        List<Train> trains = TrainRepository.findAll();
        Train targetTrain = null;
        for (Train t : trains) {
            if (t.getId().equals(trainId)) {
                targetTrain = t;
                break;
            }
        }

        if (targetTrain == null) {
            System.out.println("Error: Train not found.");
            return;
        }

        List<com.trainticket.model.Route> routes = com.trainticket.repository.RouteRepository.findAll();
        com.trainticket.model.Route targetRoute = null;
        for (com.trainticket.model.Route r : routes) {
            if (r.getId().equals(targetTrain.getRouteId())) {
                targetRoute = r;
                break;
            }
        }
        
        if (targetRoute == null) {
            System.out.println("Error: The selected train has an invalid route assignment.");
            return;
        }
        
        int fromIndex = -1, toIndex = -1;
        for (int i = 0; i < targetRoute.getStops().size(); i++) {
            if (targetRoute.getStops().get(i).getStationId().equals(fromStationId)) fromIndex = i;
            if (targetRoute.getStops().get(i).getStationId().equals(toStationId)) toIndex = i;
        }
        
        if (fromIndex == -1 || toIndex == -1) {
            System.out.println("Error: The selected stations do not belong to this train's route.");
            return;
        }
        
        if (fromIndex >= toIndex) {
            System.out.println("Error: The origin station must appear before the destination station on this route.");
            return;
        }

        int availableSeats = RouteSearchService.getAvailableSeats(targetTrain);
        if (numSeats <= 0) {
            System.out.println("Error: Must book at least 1 seat.");
            return;
        }
        if (numSeats > availableSeats) {
            System.out.println("Error: Not enough available seats. Overbooking prevented.");
            return;
        }

        List<Integer> assignedSeats = new ArrayList<>();
        int startSeat = targetTrain.getTotalSeats() - availableSeats + 1;
        for (int i = 0; i < numSeats; i++) {
            assignedSeats.add(startSeat + i);
        }

        Booking newBooking = new Booking(
                UUID.randomUUID().toString(),
                name,
                email,
                trainId,
                fromStationId,
                toStationId,
                assignedSeats,
                LocalDateTime.now().toString()
        );

        List<Booking> allBookings = BookingRepository.findAll();
        allBookings.add(newBooking);
        BookingRepository.saveAll(allBookings);

        System.out.println("Booking successful! Your booking ID is " + newBooking.getId());

        String subject = "Booking Confirmation - " + targetTrain.getName();
        String content = String.format("Hello %s,\n\nYour booking is confirmed.\nTrain: %s\nFrom: %s\nTo: %s\nSeats: %s\nBooking Time: %s",
                name, targetTrain.getName(), RouteSearchService.getStationName(fromStationId), RouteSearchService.getStationName(toStationId),
                assignedSeats.toString(), newBooking.getBookingDateTime());
        
        EmailService.sendEmail(email, subject, content);
    }
}
