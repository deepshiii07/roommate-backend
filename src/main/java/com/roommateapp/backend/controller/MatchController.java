package com.roommateapp.backend.controller;

import com.roommateapp.backend.dto.RoommateMatchResponse;
import com.roommateapp.backend.service.MatchService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/match/{id}")
    public List<RoommateMatchResponse> getMatches(@PathVariable Long id) {
        return matchService.findMatches(id);
    }
}
