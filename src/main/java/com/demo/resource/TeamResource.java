package com.demo.resource;

import com.demo.dto.utils.ResponseUtils;
import com.demo.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
@CrossOrigin("*")
public class TeamResource {
    private final TeamRepository teamRepository;

    @GetMapping("")
    public ResponseEntity<?> getTeams() {
        return ResponseUtils.ok(teamRepository.findAll());
    }
}
