DROP TABLE IF EXISTS sprinkler_state;
CREATE TABLE sprinkler_state (
                               id BIGINT PRIMARY KEY,
                               statusTime TIMESTAMP,
                               state BOOLEAN
);
DROP TABLE IF EXISTS sprinkler_report;
CREATE TABLE IF NOT EXISTS sprinkler_report (
                                   status_date DATETIME NOT NULL,
                                   state VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS weather_data;

CREATE TABLE  weather_data (
                               weather_time TIMESTAMP,
                               prediction DOUBLE PRECISION,
                               rain_measured DOUBLE PRECISION
);

INSERT INTO weather_data (weather_time, prediction, rain_measured)
VALUES
    ('2023-08-01 23:59:59', 0.215, 1.5),
    ('2023-08-02 23:59:59', 0.312,  2.7),
    ('2023-08-03 23:59:59', 0.128,  0.3),
    ('2023-08-04 23:59:59', 0.475,  3.8),
    ('2023-08-05 23:59:59', 0.056,  0.7),
    ('2023-08-06 23:59:59', 0.587,  4.2),
    ('2023-08-07 23:59:59', 0.198,  1.1),
    ('2023-08-08 23:59:59', 0.432,  2.5),
    ('2023-08-09 23:59:59', 0.149,  0.8),
    ('2023-08-10 23:59:59', 0.509,  3.4),
    ('2023-08-11 23:59:59', 0.237,  1.9),
    ('2023-08-12 23:59:59', 0.359,  2.9),
    ('2023-08-13 23:59:59', 0.087,  0.5),
    ('2023-08-14 23:59:59', 0.631,  5.0);


INSERT INTO sprinkler_state (id, statusTime, state)
VALUES
    (1, '2023-08-01 23:59:59', 0),
    (2, '2023-08-02 23:59:59', 0),
    (3, '2023-08-03 23:59:59', 1),
    (4, '2023-08-04 23:59:59', 0),
    (5, '2023-08-07 23:59:59', 1),
    (6, '2023-08-08 23:59:59', 0),
    (7, '2023-08-09 23:59:59', 1),
    (8, '2023-08-10 23:59:59', 0),
    (9, '2023-08-13 23:59:59', 0),
    (10, '2023-08-14 23:59:59', 1);


