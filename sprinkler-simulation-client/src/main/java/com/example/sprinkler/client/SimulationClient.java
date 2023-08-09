package com.example.sprinkler.client;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.sprinkler.common.DateRange;
import com.example.sprinkler.common.SimulationService;
import com.example.sprinkler.common.SprinklerEvent;
import com.example.sprinkler.common.SprinklerStatus;
import com.example.sprinkler.common.WeatherData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SimulationClient implements SimulationService {
    private final String serverApiUrl;

    private final RestTemplate template;

    public SimulationClient(String serverApiUrl, RestTemplate template) {
        this.serverApiUrl = serverApiUrl;
        this.template = template;
    }

    @Override
    public Optional<SprinklerStatus> findLatestStatus(ZonedDateTime timestamp) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("latest")
            .queryParam("timestamp", timestamp)
            .build()
            .toUri();

        ResponseEntity<SprinklerStatus> response = template.getForEntity(uri, SprinklerStatus.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    @Override
    public void updateSprinkler(SprinklerEvent event) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("update")
            .build()
            .toUri();
        template.postForLocation(uri, event);
    }

    @Override
    public DateRange findDateRange() {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("date-range")
            .build()
            .toUri();
        return template.getForObject(uri, DateRange.class);
    }

    @Override
    public List<WeatherData> listAllWeather() {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .build()
            .toUri();
        WeatherData[] result = template.getForObject(uri, WeatherData[].class);
        return result != null ? Arrays.asList(result) : Collections.emptyList();
    }

    @Override
    public Optional<WeatherData> latestWeather(ZonedDateTime timestamp) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("latest")
            .queryParam("timestamp", timestamp)
            .build()
            .toUri();
        ResponseEntity<WeatherData> response = template.getForEntity(uri, WeatherData.class);
        return response.getStatusCode().is2xxSuccessful() ?
            Optional.ofNullable(response.getBody()) :
            Optional.empty();
    }

    @Override
    public Double rainMeasuredFor(DateRange range) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("rain")
            .queryParam("start", range.start())
            .queryParam("end", range.end())
            .build()
            .toUri();
        return template.getForObject(uri, Double.class);
    }
}
