package com.trainticket.service;

import com.trainticket.model.Route;
import com.trainticket.model.StopEntry;
import com.trainticket.model.Train;
import com.trainticket.repository.RouteRepository;
import com.trainticket.repository.StationRepository;
import com.trainticket.repository.TrainRepository;
import com.trainticket.model.Station;

import java.util.List;

public class RouteSearchService {

    public static void searchConnections(String fromStationId, String toStationId) {
        List<Train> allTrains = TrainRepository.findAll();
        List<Route> allRoutes = RouteRepository.findAll();

        boolean foundAnyDirect = false;
        boolean foundAnyChangeover = false;

        for (Train train : allTrains) {
            Route route = findRouteById(allRoutes, train.getRouteId());
            if (route != null) {
                int fromIndex = indexOfStation(route, fromStationId);
                int toIndex = indexOfStation(route, toStationId);
                if (fromIndex != -1 && toIndex != -1 && fromIndex < toIndex) {
                    if (!foundAnyDirect) {
                        System.out.println("\n    Direct Connections");
                        foundAnyDirect = true;
                    }
                    StopEntry dep = route.getStops().get(fromIndex);
                    StopEntry arr = route.getStops().get(toIndex);
                    int availableSeats = getAvailableSeats(train);
                    System.out.printf("Train: %s | Departs: %s | Arrives: %s | Available Seats: %d%n",
                            train.getName(), dep.getDepartureTime(), arr.getArrivalTime(), availableSeats);
                }
            }
        }

        for (Train trainA : allTrains) {
            Route routeA = findRouteById(allRoutes, trainA.getRouteId());
            if (routeA == null) continue;
            int fromIndexA = indexOfStation(routeA, fromStationId);
            if (fromIndexA == -1) continue;

            for (int i = fromIndexA + 1; i < routeA.getStops().size(); i++) {
                StopEntry transferStopA = routeA.getStops().get(i);
                String transferStationId = transferStopA.getStationId();

                for (Train trainB : allTrains) {
                    if (trainA.getId().equals(trainB.getId())) continue;

                    Route routeB = findRouteById(allRoutes, trainB.getRouteId());
                    if (routeB == null) continue;

                    int transferIndexB = indexOfStation(routeB, transferStationId);
                    int toIndexB = indexOfStation(routeB, toStationId);

                    if (transferIndexB != -1 && toIndexB != -1 && transferIndexB < toIndexB) {
                        StopEntry transferStopB = routeB.getStops().get(transferIndexB);
                        
                        if (transferStopA.getArrivalTime().compareTo(transferStopB.getDepartureTime()) <= 0) {
                            if (!foundAnyChangeover) {
                                System.out.println("\n   1-Changeover Connections");
                                foundAnyChangeover = true;
                            }
                            StopEntry depA = routeA.getStops().get(fromIndexA);
                            StopEntry arrB = routeB.getStops().get(toIndexB);
                            
                            int seatsA = getAvailableSeats(trainA);
                            int seatsB = getAvailableSeats(trainB);

                            String stationName = getStationName(transferStationId);
                            System.out.printf("Train %s -> %s -> Train %s | Departs: %s | Arrives: %s | Seats Train 1: %d | Seats Train 2: %d%n",
                                    trainA.getName(), stationName, trainB.getName(), depA.getDepartureTime(), arrB.getArrivalTime(), seatsA, seatsB);
                        }
                    }
                }
            }
        }

        if (!foundAnyDirect && !foundAnyChangeover) {
            System.out.printf("\nNo connection found between %s and %s%n", getStationName(fromStationId), getStationName(toStationId));
        }
    }

    private static int indexOfStation(Route route, String stationId) {
        for (int i = 0; i < route.getStops().size(); i++) {
            if (route.getStops().get(i).getStationId().equals(stationId)) {
                return i;
            }
        }
        return -1;
    }

    private static Route findRouteById(List<Route> routes, String id) {
        for (Route r : routes) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }

    public static int getAvailableSeats(Train train) {
        List<com.trainticket.model.Booking> bookings = com.trainticket.repository.BookingRepository.findAll();
        int booked = 0;
        for (com.trainticket.model.Booking b : bookings) {
            if (b.getTrainId().equals(train.getId())) {
                booked += b.getSeatNumbers().size();
            }
        }
        return train.getTotalSeats() - booked;
    }

    public static String getStationName(String id) {
        List<Station> stations = StationRepository.findAll();
        for (Station s : stations) {
            if (s.getId().equals(id)) return s.getName();
        }
        return id;
    }

    public static boolean stationExists(String id) {
        List<Station> stations = StationRepository.findAll();
        for (Station s : stations) {
            if (s.getId().equals(id)) return true;
        }
        return false;
    }
}
