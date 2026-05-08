package com.trainticket.repository;

import com.trainticket.model.Station;
import java.util.List;

public class StationRepository {
    private static final JsonRepository<Station> repo = new JsonRepository<>("data/stations.json", Station[].class);

    public static List<Station> findAll() {
        return repo.findAll();
    }

    public static void saveAll(List<Station> stations) {
        repo.saveAll(stations);
    }
}
