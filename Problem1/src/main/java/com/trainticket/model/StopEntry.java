package com.trainticket.model;

import java.time.LocalTime;

public class StopEntry {
    private String stationId;
    private String departureTime;
    private String arrivalTime;

    public StopEntry() {}

    public StopEntry(String stationId, String arrivalTime, String departureTime) {
        this.stationId = stationId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
}
