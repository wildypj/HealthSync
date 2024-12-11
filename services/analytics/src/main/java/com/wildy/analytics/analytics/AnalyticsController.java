package com.wildy.analytics.analytics;

import com.wildy.analytics.analytics.services.AnalyticsService;
import com.wildy.analytics.payload.AnalyticsAggregateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/aggregate")
    public ResponseEntity<AnalyticsAggregateDTO> getAggregatedAnalytics() {
        AnalyticsAggregateDTO aggregatedData = analyticsService.getAggregatedAnalytics();
        return ResponseEntity.ok(aggregatedData);
    }
}
