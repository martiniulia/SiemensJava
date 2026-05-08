package com.trainticket.model;

import java.util.List;

public class Booking {
    private String id;
    private String customerName;
    private String customerEmail;
    private String trainId;
    private String fromStationId;
    private String toStationId;
    private List<Integer> seatNumbers;
    private String bookingDateTime;

    public Booking() {}

    public Booking(String id, String customerName, String customerEmail, String trainId,
                   String fromStationId, String toStationId, List<Integer> seatNumbers,
                   String bookingDateTime) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.trainId = trainId;
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
        this.seatNumbers = seatNumbers;
        this.bookingDateTime = bookingDateTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getFromStationId() { return fromStationId; }
    public void setFromStationId(String fromStationId) { this.fromStationId = fromStationId; }

    public String getToStationId() { return toStationId; }
    public void setToStationId(String toStationId) { this.toStationId = toStationId; }

    public List<Integer> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<Integer> seatNumbers) { this.seatNumbers = seatNumbers; }

    public String getBookingDateTime() { return bookingDateTime; }
    public void setBookingDateTime(String bookingDateTime) { this.bookingDateTime = bookingDateTime; }
}
