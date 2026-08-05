# Dataset Evidence Note — Smart Campus Service Operations Optimizer

**Prepared by:** [Your Name], [Index Number] — Level 200 (DCIT 204) data-collection sub-team
**Group:** [Group name / number]
**Date:** [Date]
**Campus modelled:** University of Ghana, Legon

## What the dataset contains

The dataset describes a campus service-and-delivery operation over the UG Legon campus and is split across four linked files: 48 locations, 100 roads, 30 resources, and 300 service requests. All identifiers are consistent across files — every road, request, and resource references a location ID that exists in `locations.csv` — and the road network forms a single connected graph so that the routing algorithms always have a valid path.

## How the locations were obtained

Locations are real, named places on the UG Legon campus known to the team: the Balme Library, Great Hall, the Computer Science and other science departments, the traditional and Diaspora halls of residence, the UG Hospital and Pharmacy, the Central Cafeteria and Night Market, banking and administrative buildings, sports grounds, and the campus gates and shuttle terminal. Each location was tagged with an area/zone and a type (Academic, Residence, Health, Dining, etc.). Approximate coordinates were laid out to preserve the real relative positions of these places and spot-checked against Google Maps.

## How the roads were obtained

Roads connect buildings that are genuine neighbours on campus, based on the team's first-hand knowledge of how one moves between them on foot. For each link, `distance_km` and `travel_time_min` were taken from Google Maps walking directions where checked, and estimated from the campus layout otherwise. The `condition_weight` reflects local knowledge of route quality (smooth main roads weighted 1.0, rougher or more congested routes higher). Each pair is listed once; both travel directions are handled by the graph loader.

## How the requests and resources were constructed

Service requests and resources are synthetic but realistic operational data — no real individuals are named or identifiable. Request categories (Medical, Security, Maintenance, IT Support, Document, Delivery, Catering) mirror real campus services. Urgency levels follow sensible patterns (Medical and Security skew urgent; Document and Catering skew routine), submission times fall within campus operating hours across a two-week window, and each deadline is a window off the submission time that tightens as urgency rises. One endpoint of each request is biased toward a fitting location (e.g. Medical involves the Hospital or Pharmacy). The 30 resources — riders, vans, shuttle buses, and maintenance crews — are homed at plausible hubs with realistic capacities and availability.

## Data protection

The dataset contains no personal data. Requests are described purely by location, category, urgency, and time — never by the name or identity of any student, patient, or staff member.

## Acknowledgement of AI assistance

An AI assistant (Claude) was used to help scaffold the location list, compute the initial road network from the campus layout, and generate the constructed request and resource records against our verified location IDs. The team reviewed all outputs, verified place names and distances against first-hand campus knowledge and Google Maps, and can explain and modify every part of the dataset.
