package com.trainticket.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private String id;
    private String name;
    private List<StopEntry> stops = new ArrayList<>();

    public Route() {}

    public Route(String id, String name, List<StopEntry> stops) {
        this.id = id;
        this.name = name;
        this.stops = stops;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<StopEntry> getStops() { return stops; }
    public void setStops(List<StopEntry> stops) { this.stops = stops; }
}
