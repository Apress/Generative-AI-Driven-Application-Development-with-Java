package com.example.opencv.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightInfo {
    private String name;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate date;
    private LocalTime time;
    private String seatNumber;
    private String greetingImageUrl = "";

    // No-arg constructor
    public FlightInfo() { }

    // All-args constructor
    public FlightInfo(String name,
                      String flightNumber,
                      String origin,
                      String destination,
                      LocalDate date,
                      LocalTime time,
                      String seatNumber) {
        this.name = name;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.seatNumber = seatNumber;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getGreetingImageUrl() {
        return greetingImageUrl;
    }

    public void setGreetingImageUrl(String greetingImageUrl) {
        this.greetingImageUrl = greetingImageUrl;
    }
}

