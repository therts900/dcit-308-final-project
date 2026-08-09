package com.ug.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ug.smartcampus.model.Request;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SchedulingServiceTest {
    @Test
    void plansHigherPriorityRequestsFirstAndBreaksTiesByTime() {
        SchedulingService service = new SchedulingService();
        service.add(request(1, 3, 10));
        service.add(request(2, 5, 12));
        service.add(request(3, 3, 8));

        assertEquals(java.util.List.of(2, 3, 1), service.plan().stream().map(Request::getRequestId).toList());
    }

    private Request request(int id, int priority, int hour) {
        return new Request(id, "Request " + id, "Tester", priority, "OPEN", 1,
                LocalDateTime.of(2026, 7, 1, hour, 0), "Estates");
    }
}
