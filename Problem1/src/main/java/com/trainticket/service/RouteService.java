package com.trainticket.service;

import com.trainticket.model.Route;
import com.trainticket.repository.RouteRepository;

import java.util.List;

public class RouteService {
    public static void addRoute(Route route) {
        List<Route> routes = RouteRepository.findAll();
        routes.add(route);
        RouteRepository.saveAll(routes);
        System.out.println("Route added successfully.");
    }

    public static void removeRoute(String id) {
        List<Route> routes = RouteRepository.findAll();
        boolean removed = routes.removeIf(r -> r.getId().equals(id));
        if (removed) {
            RouteRepository.saveAll(routes);
            System.out.println("Route removed successfully.");
        } else {
            System.out.println("Route not found.");
        }
    }

    public static void updateRoute(Route updatedRoute) {
        List<Route> routes = RouteRepository.findAll();
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getId().equals(updatedRoute.getId())) {
                routes.set(i, updatedRoute);
                RouteRepository.saveAll(routes);
                System.out.println("Route updated successfully.");
                return;
            }
        }
        System.out.println("Route not found.");
    }
}
