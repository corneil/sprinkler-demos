package io.spring.sprinkler.client;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulation.client")
public class SimulationClientProperties {
    private String serverApiUrl = "http://localhost:8080";

    public String getServerApiUrl() {
        return serverApiUrl;
    }

    public void setServerApiUrl(String serverApiUrl) {
        this.serverApiUrl = serverApiUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimulationClientProperties that = (SimulationClientProperties) o;

        return Objects.equals(serverApiUrl, that.serverApiUrl);
    }

    @Override
    public int hashCode() {
        return serverApiUrl != null ? serverApiUrl.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SimulationClientProperties{" +
            "serverApiUrl='" + serverApiUrl + '\'' +
            '}';
    }
}
