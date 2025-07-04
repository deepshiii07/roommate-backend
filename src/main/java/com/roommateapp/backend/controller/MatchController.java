package com.roommateapp.backend.controller;

import com.roommateapp.backend.model.User;
import com.roommateapp.backend.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/match/{id}")
    public List<User> getMatches(@PathVariable Long id) {
        return matchService.findMatches(id);
    }
}
