package com.example.sprinkler.app;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.WeatherData;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SprinklerSimulatorController {
    private final SimulationService simulationService;

    public SprinklerSimulatorController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }


    @GetMapping(value = "latest", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public Optional<SprinklerStatus> findLatestStatus(@RequestParam("timestamp") ZonedDateTime timestamp) {
        return simulationService.findLatestStatus(timestamp);
    }


    @PostMapping(value = "update", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public void updateSprinkler(@RequestBody SprinklerEvent event) {
        simulationService.updateSprinkler(event);
    }

    @GetMapping(value = "date-range", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public DateRange findDateRange() {
        return simulationService.findDateRange();
    }

    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<WeatherData> listAllWeather() {
        return simulationService.listAllWeather();
    }

    @GetMapping(value = "latest-on")
    public Optional<WeatherData> latestWeather(@RequestParam("timestamp") ZonedDateTime timestamp) {
        return simulationService.latestWeather(timestamp);
    }


    @GetMapping(value = "rain", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public Double rainMeasuredFor(@RequestParam("start") ZonedDateTime start, @RequestParam("end") ZonedDateTime end) {
        return simulationService.rainMeasuredFor(new DateRange(start, end));
    }
}
