package com.example.githubtracker.controller;

import com.example.githubtracker.model.GitHubEvent;
import com.example.githubtracker.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final GitHubService gitHubService;

    @Autowired
    public ActivityController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    /**
     * Get recent activity for a GitHub user.
     * @param username The GitHub username
     * @return List of formatted activity strings
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserActivity(@PathVariable String username) {
        // Check if user exists first
        if (!gitHubService.userExists(username)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User '" + username + "' not found");
            return ResponseEntity.status(404).body(error);
        }

        List<GitHubEvent> events = gitHubService.fetchActivity(username);
        
        if (events.isEmpty()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "No recent activity found for user '" + username + "'");
            return ResponseEntity.ok(message);
        }

        List<String> formattedActivity = events.stream()
                .map(this::formatEvent)
                .filter(s -> s != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(formattedActivity);
    }

    /**
     * Get raw GitHub events (JSON format).
     * @param username The GitHub username
     * @return List of GitHubEvent objects
     */
    @GetMapping("/{username}/raw")
    public ResponseEntity<?> getRawActivity(@PathVariable String username) {
        if (!gitHubService.userExists(username)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User '" + username + "' not found");
            return ResponseEntity.status(404).body(error);
        }

        List<GitHubEvent> events = gitHubService.fetchActivity(username);
        return ResponseEntity.ok(events);
    }

    /**
     * Formats a GitHub event into a readable string.
     */
    private String formatEvent(GitHubEvent event) {
        String repoName = event.getRepo() != null ? event.getRepo().getName() : "unknown";
        
        switch (event.getType()) {
            case "PushEvent":
                return "- Pushed commits to " + repoName;
            case "IssuesEvent":
                return "- Interacted with an issue in " + repoName;
            case "WatchEvent":
                return "- Starred " + repoName;
            case "ForkEvent":
                return "- Forked " + repoName;
            case "CreateEvent":
                return "- Created repository " + repoName;
            case "PullRequestEvent":
                return "- Opened a pull request in " + repoName;
            case "IssueCommentEvent":
                return "- Commented on an issue in " + repoName;
            case "DeleteEvent":
                return "- Deleted something in " + repoName;
            case "PublicEvent":
                return "- Made " + repoName + " public";
            default:
                return "- " + event.getType() + " in " + repoName;
        }
    }
}
