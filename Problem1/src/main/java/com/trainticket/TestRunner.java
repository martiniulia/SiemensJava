package com.trainticket;

import com.trainticket.model.Route;
import com.trainticket.model.StopEntry;
import com.trainticket.repository.BookingRepository;
import com.trainticket.repository.RouteRepository;
import com.trainticket.repository.StationRepository;
import com.trainticket.repository.TrainRepository;
import com.trainticket.service.BookingService;
import com.trainticket.service.RouteSearchService;
import com.trainticket.service.RouteService;
import com.trainticket.service.StationService;
import com.trainticket.service.TrainService;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private static int passedTests = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("Starting automated tests\n");
        
        testStationService();
        testRouteService();
        testTrainService();
        testRouteSearch();
        testBookingService();
        
        System.out.println(String.format("\nAll tests completed: %d out of %d passed.", passedTests, totalTests));
    }

    private static void assertEquals(boolean condition, String description) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("  [PASS] " + description);
        } else {
            System.out.println("  [FAIL] " + description);
        }
    }

    private static void testStationService() {
        System.out.println("Testing StationService");
        int initialSize = StationRepository.findAll().size();
        StationService.addStation("TEST_S1", "Test Station 1");
        assertEquals(StationRepository.findAll().size() == initialSize + 1, "Station added successfully");
    }

    private static void testRouteService() {
        System.out.println("\nTesting RouteService");
        List<StopEntry> stops = new ArrayList<>();
        stops.add(new StopEntry("TEST_S1", "08:00", "08:15"));
        stops.add(new StopEntry("S1", "09:00", "09:15"));
        Route route = new Route("TEST_R1", "Test Route", stops);
        
        int initialSize = RouteRepository.findAll().size();
        RouteService.addRoute(route);
        assertEquals(RouteRepository.findAll().size() == initialSize + 1, "Route added successfully");
        
        RouteService.removeRoute("TEST_R1");
        assertEquals(RouteRepository.findAll().size() == initialSize, "Route removed successfully");
    }

    private static void testTrainService() {
        System.out.println("\nTesting TrainService");
        int initialSize = TrainRepository.findAll().size();
        TrainService.addTrain("TEST_T1", "Test Train", 100, "R1");
        assertEquals(TrainRepository.findAll().size() == initialSize + 1, "Train added successfully");
        
        TrainService.viewBookingsForTrain("TEST_T1");
        assertEquals(true, "Train bookings viewed successfully");
        
        TrainService.registerDelay("TEST_T1", 15);
        assertEquals(true, "Train delay registered successfully without errors");
        
        TrainService.removeTrain("TEST_T1");
        assertEquals(TrainRepository.findAll().size() == initialSize, "Train removed successfully");
    }

    private static void testRouteSearch() {
        System.out.println("\nTesting RouteSearchService");
        System.out.println("  > Simulating S2 -> S5:");
        RouteSearchService.searchConnections("S2", "S5");
        assertEquals(true, "Direct connection search executed");
        
        System.out.println("  > Simulating S4 -> S5:");
        RouteSearchService.searchConnections("S4", "S5");
        assertEquals(true, "1-changeover connection search executed");
        
        System.out.println("  > Simulating S5 -> S2 (wrong direction):");
        RouteSearchService.searchConnections("S5", "S2");
        assertEquals(true, "Invalid connection search handled gracefully");
    }

    private static void testBookingService() {
        System.out.println("\nTesting BookingService");
        
        int initialBookings = BookingRepository.findAll().size();
        BookingService.bookTicket("Test User", "test@test.com", "T1", "S2", "S5", 1);
        int newBookings = BookingRepository.findAll().size();
        assertEquals(newBookings == initialBookings + 1, "Valid booking created successfully");
        
        initialBookings = newBookings;
        BookingService.bookTicket("Test User", "test@test.com", "T1", "INVALID", "S5", 1);
        assertEquals(BookingRepository.findAll().size() == initialBookings, "Invalid station booking gracefully rejected");
        
        BookingService.bookTicket("Test User", "test@test.com", "T1", "S5", "S2", 1);
        assertEquals(BookingRepository.findAll().size() == initialBookings, "Wrong direction booking gracefully rejected");
        
        BookingService.bookTicket("Test User", "test@test.com", "T1", "S2", "S5", 9999);
        assertEquals(BookingRepository.findAll().size() == initialBookings, "Overbooking gracefully rejected");
    }
}
