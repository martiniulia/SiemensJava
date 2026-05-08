package com.trainticket.ui;

import com.trainticket.model.Station;
import com.trainticket.model.Train;
import com.trainticket.repository.StationRepository;
import com.trainticket.repository.TrainRepository;
import com.trainticket.service.BookingService;
import com.trainticket.service.RouteSearchService;
import com.trainticket.util.InputHelper;

import java.util.List;

public class CustomerMenu {
    public static void start() {
        while (true) {
            System.out.println("\n   Customer Menu");
            System.out.println("1. Search Connections");
            System.out.println("2. Book a Ticket");
            System.out.println("0. Back to Main Menu");

            int choice = InputHelper.readInt("Select an option: ");

            if (choice == 1) {
                printAvailableStations();
                String from = InputHelper.readString("Enter Origin Station ID: ");
                String to = InputHelper.readString("Enter Destination Station ID: ");
                RouteSearchService.searchConnections(from, to);
            } else if (choice == 2) {
                printAvailableTrains();
                String trainId = InputHelper.readString("Enter Train ID: ");
                
                Train targetTrain = null;
                for (Train t : TrainRepository.findAll()) {
                    if (t.getId().equals(trainId)) {
                        targetTrain = t;
                        break;
                    }
                }
                
                if (targetTrain == null) {
                    System.out.println("Error: Train not found.");
                    continue;
                }
                
                com.trainticket.model.Route targetRoute = null;
                for (com.trainticket.model.Route r : com.trainticket.repository.RouteRepository.findAll()) {
                    if (r.getId().equals(targetTrain.getRouteId())) {
                        targetRoute = r;
                        break;
                    }
                }
                
                if (targetRoute != null) {
                    System.out.println("\nStations available for Train " + trainId + ":");
                    for (com.trainticket.model.StopEntry stop : targetRoute.getStops()) {
                        String sName = RouteSearchService.getStationName(stop.getStationId());
                        System.out.println("- " + stop.getStationId() + " (" + sName + ")");
                    }
                    System.out.println();
                } else {
                    System.out.println("Warning: Could not load route information for this train.");
                }

                String from = InputHelper.readString("Enter Origin Station ID: ");
                String to = InputHelper.readString("Enter Destination Station ID: ");
                
                int seats = InputHelper.readInt("Enter number of seats to book: ");
                String name = InputHelper.readString("Enter your Name: ");
                String email = InputHelper.readString("Enter your Email: ");

                BookingService.bookTicket(name, email, trainId, from, to, seats);
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void printAvailableStations() {
        System.out.println("\nAvailable Stations:");
        List<Station> stations = StationRepository.findAll();
        for (Station s : stations) {
            System.out.println("- " + s.getId() + " (" + s.getName() + ")");
        }
        System.out.println();
    }

    private static void printAvailableTrains() {
        System.out.println("\nAvailable Trains:");
        List<Train> trains = TrainRepository.findAll();
        for (Train t : trains) {
            System.out.println("- " + t.getId() + " (" + t.getName() + ") - Route ID: " + t.getRouteId());
        }
        System.out.println();
    }
}
