package com.ug.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class NavigationServiceTest {
    @Test
    void loadsExtractedNetworkAndComputesWalkingRoute() throws Exception {
        NavigationService navigation = new NavigationService(Path.of("database/data"));
        assertTrue(navigation.nodeCount() >= 1_500);
        assertTrue(navigation.edgeCount() >= 1_900);
        var destinations = navigation.destinations();
        assertTrue(destinations.size() >= 400);

        NavigationService.Route route = navigation.route(destinations.get(0).id(),
                destinations.get(destinations.size() - 1).id());
        assertFalse(route.path().isEmpty());
        assertTrue(Double.isFinite(route.travelTimeMinutes()));
        assertTrue(route.travelTimeMinutes() > 0);
        assertTrue(route.distanceKm() > 0);
    }
}
