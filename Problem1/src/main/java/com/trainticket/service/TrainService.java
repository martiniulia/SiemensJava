package com.trainticket.service;

import com.trainticket.model.Booking;
import com.trainticket.model.Route;
import com.trainticket.model.StopEntry;
import com.trainticket.model.Train;
import com.trainticket.repository.BookingRepository;
import com.trainticket.repository.RouteRepository;
import com.trainticket.repository.TrainRepository;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrainService {

    public static void viewBookingsForTrain(String trainId) {
        List<Booking> bookings = BookingRepository.findAll();
        System.out.println("\n   Bookings for Train " + trainId);
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getTrainId().equals(trainId)) {
                found = true;
                System.out.printf("Booking ID: %s | Customer: %s (%s) | Seats: %s%n",
                        b.getId(), b.getCustomerName(), b.getCustomerEmail(), b.getSeatNumbers().toString());
            }
        }
        if (!found) {
            System.out.println("No bookings found for this train");
        }
    }

    public static void registerDelay(String trainId, int delayMinutes) {
        List<Train> trains = TrainRepository.findAll();
        Train targetTrain = null;
        for (Train t : trains) {
            if (t.getId().equals(trainId)) {
                targetTrain = t;
                break;
            }
        }

        if (targetTrain == null) {
            System.out.println("Error: Train not found");
            return;
        }

        List<Route> routes = RouteRepository.findAll();
        Route targetRoute = null;
        for (Route r : routes) {
            if (r.getId().equals(targetTrain.getRouteId())) {
                targetRoute = r;
                break;
            }
        }

        if (targetRoute == null || targetRoute.getStops().isEmpty()) {
            System.out.println("Error: Route not found or invalid");
            return;
        }

        String originalDepTimeStr = targetRoute.getStops().get(0).getDepartureTime();
        LocalTime originalTime = LocalTime.parse(originalDepTimeStr);
        LocalTime delayedTime = originalTime.plusMinutes(delayMinutes);

        System.out.println("Train " + targetTrain.getName() + " marked as delayed by " + delayMinutes + " minutes");

        List<Booking> bookings = BookingRepository.findAll();
        Set<String> notifiedEmails = new HashSet<>();

        for (Booking b : bookings) {
            if (b.getTrainId().equals(trainId)) {
                if (!notifiedEmails.contains(b.getCustomerEmail())) {
                    String subject = "Delay Notification - " + targetTrain.getName();
                    String content = String.format("Dear %s,\n\nTrain %s is delayed by %d minutes.\nOriginal Departure Time: %s\nNew Estimated Departure Time: %s\n\nWe apologize for the inconvenience.",
                            b.getCustomerName(), targetTrain.getName(), delayMinutes, originalDepTimeStr, delayedTime.toString());
                    
                    EmailService.sendEmail(b.getCustomerEmail(), subject, content);
                    notifiedEmails.add(b.getCustomerEmail());
                }
            }
        }
        System.out.println("Customers notified: " + notifiedEmails.size());
    }

    public static void addTrain(String id, String name, int seats, String routeId) {
        List<Train> trains = TrainRepository.findAll();
        trains.add(new Train(id, name, seats, routeId));
        TrainRepository.saveAll(trains);
        System.out.println("Train added");
    }
    
    public static void removeTrain(String id) {
        List<Train> trains = TrainRepository.findAll();
        boolean removed = trains.removeIf(t -> t.getId().equals(id));
        if (removed) {
            TrainRepository.saveAll(trains);
            System.out.println("Train removed");
        } else {
            System.out.println("Train not found");
        }
    }
}
