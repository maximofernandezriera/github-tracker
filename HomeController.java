package com.example.githubtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "GitHub Activity Tracker");
        response.put("version", "1.0.0");
        response.put("endpoints", new String[]{
            "GET /api/activity/{username} - Get formatted activity",
            "GET /api/activity/{username}/raw - Get raw JSON events"
        });
        response.put("example", "http://localhost:8081/api/activity/kamranahmedse");
        return response;
    }
}
