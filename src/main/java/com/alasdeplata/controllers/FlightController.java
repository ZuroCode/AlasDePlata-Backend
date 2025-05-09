package com.alasdeplata.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alasdeplata.dto.flight.FlightDetailsResponse;
import com.alasdeplata.dto.flight.FlightRequest;
import com.alasdeplata.dto.flight.FlightRequestFilter;
import com.alasdeplata.dto.flight.FlightResponse;
import com.alasdeplata.dto.flight.FlightUpdateRequest;
import com.alasdeplata.enums.FlightClass;
import com.alasdeplata.services.FlightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAll() {
        return new ResponseEntity<>(flightService.getAllFlights(), HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<List<FlightDetailsResponse>> getAllDetails() {
        return ResponseEntity.ok(flightService.getAllFlightDetails());
    }

    @GetMapping("/details/search")
    public ResponseEntity<List<FlightDetailsResponse>> searchFlightDetails(
            @ModelAttribute FlightRequestFilter flightRequestFilter) {
        return ResponseEntity.ok(flightService.searchFlightDetails(flightRequestFilter));
    }

    @GetMapping("/classes")
    public ResponseEntity<FlightClass[]> getFlightClasses() {
        return ResponseEntity.ok(FlightClass.values());
    }

    @GetMapping("{id}")
    public ResponseEntity<FlightResponse> getById(@PathVariable() Long id) {
        Optional<FlightResponse> flightOptional = flightService.getFlightById(id);

        if (flightOptional.isPresent()) {
            return new ResponseEntity<>(flightOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightRequest flightRequest) {
        FlightResponse flightResponse = flightService.createFlight(flightRequest);
        return new ResponseEntity<>(flightResponse, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<FlightResponse> update(@PathVariable() Long id,
            @RequestBody FlightUpdateRequest flightRequest) {
        Optional<FlightResponse> existingItemOptional = flightService.getFlightById(id);
        if (existingItemOptional.isPresent()) {
            FlightResponse flightResponse = flightService.updateFlight(id, flightRequest);
            return new ResponseEntity<>(flightResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable() Long id) {
        flightService.deleteFlight(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
