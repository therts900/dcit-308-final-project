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
    priority INTEGER NOT NULL CHECK (
        priority BETWEEN 1 AND 5
    ),
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
-- Real campus data model (locations, roads, service requests, resources) populated
-- from the group's data-collection CSVs, distinct from the small demo tables above.
-- IDs are prefixed strings as collected (L001, R001, Q001, V001...), not integers.
CREATE TABLE IF NOT EXISTS locations (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    area VARCHAR(60) NOT NULL,
    location_type VARCHAR(40) NOT NULL,
    x_coord REAL NOT NULL,
    y_coord REAL NOT NULL
);
CREATE TABLE IF NOT EXISTS roads (
    id VARCHAR(10) PRIMARY KEY,
    from_location_id VARCHAR(10) NOT NULL,
    to_location_id VARCHAR(10) NOT NULL,
    distance_km REAL NOT NULL CHECK (distance_km >= 0),
    travel_time_min REAL NOT NULL CHECK (travel_time_min >= 0),
    condition_weight REAL NOT NULL,
    FOREIGN KEY (from_location_id) REFERENCES locations(id),
    FOREIGN KEY (to_location_id) REFERENCES locations(id)
);
CREATE TABLE IF NOT EXISTS service_requests (
    id VARCHAR(10) PRIMARY KEY,
    source_location_id VARCHAR(10) NOT NULL,
    destination_location_id VARCHAR(10) NOT NULL,
    category VARCHAR(40) NOT NULL,
    urgency INTEGER NOT NULL CHECK (
        urgency BETWEEN 1 AND 5
    ),
    time_submitted TIMESTAMP NOT NULL,
    deadline TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    FOREIGN KEY (source_location_id) REFERENCES locations(id),
    FOREIGN KEY (destination_location_id) REFERENCES locations(id)
);
-- Distinct from the legacy demo "resources" table above (which is keyed to the
-- toy "buildings" table). This matches the real resources.csv (riders, vans,
-- shuttle buses, maintenance crews) keyed to the real locations table.
CREATE TABLE IF NOT EXISTS campus_resources (
    id VARCHAR(10) PRIMARY KEY,
    resource_type VARCHAR(40) NOT NULL,
    home_location_id VARCHAR(10) NOT NULL,
    capacity INTEGER NOT NULL,
    availability_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (home_location_id) REFERENCES locations(id)
);
-- Owned by the Level 300 algorithm-experiments workstream
-- Created here so the data-collection migration doesn't block their later inserts
CREATE TABLE IF NOT EXISTS algorithm_runs (
    id INTEGER PRIMARY KEY,
    algorithm_name VARCHAR(60) NOT NULL,
    parameters TEXT,
    run_at TIMESTAMP NOT NULL,
    result_summary TEXT
);