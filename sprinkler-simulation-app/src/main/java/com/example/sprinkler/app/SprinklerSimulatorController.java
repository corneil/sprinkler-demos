package com.example.sprinkler.app;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.WeatherData;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class SprinklerSimulatorController {
    private final SimulationService simulationService;

    public SprinklerSimulatorController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public static class RootResource extends RepresentationModel<RootResource> {

        private String version;

        // For JSON un-marshalling
        private RootResource() {
        }

        public RootResource(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<RootResource> root() {
        RootResource resource = new RootResource("1.0.0-SNAPSHOT");
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).root()).withSelfRel());
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).findDateRange()).withRel("dateRange"));
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).latestWeather(null)).withRel("latest"));
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).listAllWeather()).withRel("weather"));
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).listAllStatus()).withRel("status"));
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).rainMeasuredFor(null, null)).withRel("rain"));
        resource.add(linkTo(methodOn(SprinklerSimulatorController.class).updateSprinkler(null)).withRel("update"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping(value = "latest", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<SprinklerStatus> findLatestStatus(@RequestParam("timestamp") ZonedDateTime timestamp) {
        Optional<SprinklerStatus> status = simulationService.findLatestStatus(timestamp);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "status", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SprinklerStatus>> listAllStatus() {
        return ResponseEntity.ok(simulationService.listAllStatus());
    }


    @PostMapping(value = "update", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateSprinkler(@RequestBody SprinklerEvent event) {
        simulationService.updateSprinkler(event);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "date-range", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<DateRange> findDateRange() {
        return ResponseEntity.ok(simulationService.findDateRange());
    }

    @GetMapping(value = "weather", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeatherData>> listAllWeather() {
        return ResponseEntity.ok(simulationService.listAllWeather());
    }

    @GetMapping(value = "latest-on")
    public ResponseEntity<WeatherData> latestWeather(@RequestParam("timestamp") ZonedDateTime timestamp) {
        Optional<WeatherData> weatherData = simulationService.latestWeather(timestamp);
        return weatherData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(value = "rain", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rainMeasuredFor(@RequestParam("start") ZonedDateTime start, @RequestParam("end") ZonedDateTime end) {
        Double measured = simulationService.rainMeasuredFor(new DateRange(start, end));
        if (measured != null) {
            return ResponseEntity.ok(String.valueOf(measured));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
