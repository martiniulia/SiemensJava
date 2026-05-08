package com.trainticket.service;

import com.trainticket.model.Station;
import com.trainticket.repository.StationRepository;

import java.util.List;

public class StationService {
    public static void addStation(String id, String name) {
        List<Station> stations = StationRepository.findAll();
        stations.add(new Station(id, name));
        StationRepository.saveAll(stations);
        System.out.println("Station added successfully.");
    }
}
