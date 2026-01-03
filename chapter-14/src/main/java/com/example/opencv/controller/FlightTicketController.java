package com.example.opencv.controller;

import com.example.opencv.model.FlightInfo;
import com.example.opencv.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/flight")
public class FlightTicketController {

    private final ScanService scanService;

    @Autowired
    public FlightTicketController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/scan")
    public ResponseEntity<FlightInfo> scanTicket() throws IOException {
        // 1. Load the image from src/main/resources/flight_ticket.jpg
        Resource imgRes = new ClassPathResource("flight_ticket.jpg");

        // 2. Call the service
        FlightInfo info = scanService.scanImage(imgRes);

        // 3. Return the parsed fields
        return ResponseEntity.ok(info);
    }
}
