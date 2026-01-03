package com.example.demo.controller;

public class FlightTicketRequest {

    private String passengerName;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String flightDate;
    private String seatNumber;

    public FlightTicketRequest() {
    }

    public FlightTicketRequest(String passengerName, String flightNumber, String departure, String arrival,
                               String flightDate, String seatNumber) {
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.flightDate = flightDate;
        this.seatNumber = seatNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
