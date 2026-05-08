package com.trainticket.ui;

import com.trainticket.config.AppConfig;
import com.trainticket.model.Route;
import com.trainticket.model.StopEntry;
import com.trainticket.model.Train;
import com.trainticket.repository.RouteRepository;
import com.trainticket.repository.TrainRepository;
import com.trainticket.service.RouteSearchService;
import com.trainticket.service.RouteService;
import com.trainticket.service.TrainService;
import com.trainticket.util.InputHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {
    public static void start() {
        String pwd = InputHelper.readString("Enter Admin Password: ");
        if (!AppConfig.ADMIN_PASSWORD.equals(pwd)) {
            System.out.println("Incorrect password!");
            return;
        }

        while (true) {
            System.out.println("\n   Admin Menu");
            System.out.println("1. Add Route");
            System.out.println("2. Remove Route");
            System.out.println("3. Add Train");
            System.out.println("4. Remove Train");
            System.out.println("5. View Bookings for Train");
            System.out.println("6. Mark Train as Delayed");
            System.out.println("7. Add Station");
            System.out.println("0. Back to Main Menu");

            int choice = InputHelper.readInt("Select an option: ");

            if (choice == 1) {
                printExistingRoutes();
                String id = InputHelper.readString("Enter Route ID: ");
                if (routeExists(id)) {
                    System.out.println("Error: A Route with this ID already exists.");
                    continue;
                }
                String name = InputHelper.readString("Enter Route Name: ");
                int numStops = InputHelper.readInt("How many stops? ");
                printExistingStations();
                List<StopEntry> stops = new ArrayList<>();
                for (int i = 0; i < numStops; i++) {
                    System.out.println("Stop " + (i + 1) + ":");
                    String stationId = InputHelper.readString("Station ID: ");
                    if (!RouteSearchService.stationExists(stationId)) {
                        System.out.println("Warning: Station ID does not exist in the system yet. You may want to add it later.");
                    }
                    String arrTime = readTime("Arrival Time (HH:mm): ");
                    String depTime = readTime("Departure Time (HH:mm): ");
                    stops.add(new StopEntry(stationId, arrTime, depTime));
                }
                RouteService.addRoute(new Route(id, name, stops));
            } else if (choice == 2) {
                printExistingRoutes();
                String id = InputHelper.readString("Enter Route ID to remove: ");
                RouteService.removeRoute(id);
            } else if (choice == 3) {
                printExistingTrains();
                printExistingRoutes();
                String routeId = InputHelper.readString("Enter Assigned Route ID: ");
                if (!routeExists(routeId)) {
                    System.out.println("Error: Route ID does not exist. Please create the route first.");
                    continue;
                }
                String id = InputHelper.readString("Enter Train ID: ");
                if (trainExists(id)) {
                    System.out.println("Error: A Train with this ID already exists.");
                    continue;
                }
                String name = InputHelper.readString("Enter Train Name: ");
                int capacity = InputHelper.readInt("Enter Total Seats: ");
                TrainService.addTrain(id, name, capacity, routeId);
            } else if (choice == 4) {
                printExistingTrains();
                String id = InputHelper.readString("Enter Train ID to remove: ");
                TrainService.removeTrain(id);
            } else if (choice == 5) {
                printExistingTrains();
                String trainId = InputHelper.readString("Enter Train ID: ");
                TrainService.viewBookingsForTrain(trainId);
            } else if (choice == 6) {
                printExistingTrains();
                String trainId = InputHelper.readString("Enter Train ID: ");
                int delay = InputHelper.readInt("Enter Delay (minutes): ");
                TrainService.registerDelay(trainId, delay);
            } else if (choice == 7) {
                printExistingStations();
                String id = InputHelper.readString("Enter Station ID: ");
                if (RouteSearchService.stationExists(id)) {
                    System.out.println("Error: A Station with this ID already exists.");
                    continue;
                }
                String name = InputHelper.readString("Enter Station Name: ");
                com.trainticket.service.StationService.addStation(id, name);
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static String readTime(String prompt) {
        while (true) {
            String time = InputHelper.readString(prompt);
            if (time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                return time;
            }
            System.out.println("Invalid time format. Please use HH:mm (e.g. 08:30 or 14:45).");
        }
    }

    private static boolean routeExists(String id) {
        List<Route> routes = RouteRepository.findAll();
        for (Route r : routes) {
            if (r.getId().equals(id)) return true;
        }
        return false;
    }

    private static boolean trainExists(String id) {
        List<Train> trains = TrainRepository.findAll();
        for (Train t : trains) {
            if (t.getId().equals(id)) return true;
        }
        return false;
    }

    private static void printExistingRoutes() {
        System.out.println("\nExisting Routes:");
        List<Route> routes = RouteRepository.findAll();
        for (Route r : routes) {
            System.out.println("- " + r.getId() + " (" + r.getName() + ")");
        }
        System.out.println();
    }

    private static void printExistingTrains() {
        System.out.println("\nExisting Trains:");
        List<Train> trains = TrainRepository.findAll();
        for (Train t : trains) {
            System.out.println("- " + t.getId() + " (" + t.getName() + ") - Route ID: " + t.getRouteId());
        }
        System.out.println();
    }

    private static void printExistingStations() {
        System.out.println("\nExisting Stations:");
        List<com.trainticket.model.Station> stations = com.trainticket.repository.StationRepository.findAll();
        for (com.trainticket.model.Station s : stations) {
            System.out.println("- " + s.getId() + " (" + s.getName() + ")");
        }
        System.out.println();
    }
}
