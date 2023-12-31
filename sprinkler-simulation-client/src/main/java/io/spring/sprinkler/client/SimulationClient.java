package io.spring.sprinkler.client;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.spring.sprinkler.common.DateRange;
import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerEvent;
import io.spring.sprinkler.common.SprinklerHistory;
import io.spring.sprinkler.common.SprinklerStatus;
import io.spring.sprinkler.common.WeatherData;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
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
    public Optional<SprinklerStatus> findLatestStatus(@NonNull ZonedDateTime timestamp) {
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
    public void resetState() {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("reset")
            .build()
            .toUri();
        template.postForLocation(uri, null);
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
            .pathSegment("weather")
            .build()
            .toUri();
        WeatherData[] result = template.getForObject(uri, WeatherData[].class);
        return result != null ? Arrays.asList(result) : Collections.emptyList();
    }

    @Override
    public Optional<WeatherData> latestWeather(@NonNull ZonedDateTime timestamp) {
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
            .queryParam("start", range.getStart())
            .queryParam("end", range.getEnd())
            .build()
            .toUri();
        return template.getForObject(uri, Double.class);
    }

    @Override
    public List<SprinklerStatus> listAllStatus() {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("status")
            .build()
            .toUri();
        ResponseEntity<SprinklerStatus[]> response = template.getForEntity(uri, SprinklerStatus[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Assert.notNull(response.getBody(), "Expected body");
            return Arrays.asList(response.getBody());
        }
        return Collections.emptyList();
    }

    @Override
    public List<SprinklerHistory> listHistory() {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverApiUrl)
            .pathSegment("history")
            .build()
            .toUri();
        ResponseEntity<SprinklerHistory[]> response = template.getForEntity(uri, SprinklerHistory[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Assert.notNull(response.getBody(), "Expected body");
            return Arrays.asList(response.getBody());
        }
        return Collections.emptyList();
    }
}
