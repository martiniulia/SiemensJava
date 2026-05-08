package com.trainticket.repository;

import com.trainticket.model.Route;
import java.util.List;

public class RouteRepository {
    private static final JsonRepository<Route> repo = new JsonRepository<>("data/routes.json", Route[].class);

    public static List<Route> findAll() {
        return repo.findAll();
    }

    public static void saveAll(List<Route> routes) {
        repo.saveAll(routes);
    }
}
