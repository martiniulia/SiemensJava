package com.trainticket.repository;

import com.trainticket.model.Train;
import java.util.List;

public class TrainRepository {
    private static final JsonRepository<Train> repo = new JsonRepository<>("data/trains.json", Train[].class);

    public static List<Train> findAll() {
        return repo.findAll();
    }

    public static void saveAll(List<Train> trains) {
        repo.saveAll(trains);
    }
}
