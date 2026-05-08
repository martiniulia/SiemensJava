package com.trainticket.model;

public class Train {
    private String id;
    private String name;
    private int totalSeats;
    private String routeId;

    public Train() {}

    public Train(String id, String name, int totalSeats, String routeId) {
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
        this.routeId = routeId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
}
