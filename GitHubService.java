package com.example.githubtracker.service;

import com.example.githubtracker.model.GitHubEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    
    @Value("${github.api.url:https://api.github.com/users/%s/events}")
    private String githubApiUrl;

    public GitHubService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches recent activity for a GitHub user.
     * @param username The GitHub username
     * @return List of GitHub events, or empty list if user not found or error occurs
     */
    public List<GitHubEvent> fetchActivity(String username) {
        String url = String.format(githubApiUrl, username);
        
        try {
            GitHubEvent[] events = restTemplate.getForObject(url, GitHubEvent[].class);
            return events != null ? Arrays.asList(events) : Collections.emptyList();
        } catch (HttpClientErrorException.NotFound e) {
            // User not found (404)
            return Collections.emptyList();
        } catch (RestClientException e) {
            // Other errors (network, rate limit, etc.)
            throw new RuntimeException("Failed to fetch GitHub activity: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a GitHub user exists.
     * @param username The GitHub username
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String username) {
        String url = "https://api.github.com/users/" + username;
        
        try {
            restTemplate.headForHeaders(url);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
