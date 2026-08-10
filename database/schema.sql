-- SQLite schema for the Smart Campus Service Operations Optimizer.
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS buildings (
    building_id INTEGER PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    x_coord REAL NOT NULL,
    y_coord REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS resources (
    resource_id INTEGER PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(60) NOT NULL,
    capabilities TEXT,
    location_building_id INTEGER,
    FOREIGN KEY (location_building_id) REFERENCES buildings(building_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id INTEGER PRIMARY KEY,
    description TEXT NOT NULL,
    requester VARCHAR(120) NOT NULL,
    priority INTEGER NOT NULL CHECK (priority BETWEEN 1 AND 5),
    status VARCHAR(30) NOT NULL,
    building_id INTEGER NOT NULL,
    requested_time TIMESTAMP NOT NULL,
    department VARCHAR(120),
    FOREIGN KEY (building_id) REFERENCES buildings(building_id)
);

CREATE TABLE IF NOT EXISTS edges (
    from_building INTEGER NOT NULL,
    to_building INTEGER NOT NULL,
    distance REAL NOT NULL CHECK (distance >= 0),
    PRIMARY KEY (from_building, to_building),
    FOREIGN KEY (from_building) REFERENCES buildings(building_id),
    FOREIGN KEY (to_building) REFERENCES buildings(building_id)
);

CREATE TABLE IF NOT EXISTS schedule_entries (
    schedule_entry_id INTEGER PRIMARY KEY,
    request_id INTEGER NOT NULL,
    resource_id INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    CHECK (end_time > start_time),
    FOREIGN KEY (request_id) REFERENCES requests(request_id),
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
);
