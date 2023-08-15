package io.spring.sprinkler.common;

import java.time.ZonedDateTime;

public record DateRange(ZonedDateTime start, ZonedDateTime end) {


    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DateRange{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }
}
