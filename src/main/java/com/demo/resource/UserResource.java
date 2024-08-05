package com.demo.resource;

import com.demo.dto.request.RemoveUsersReq;
import com.demo.dto.request.UpsertUserReq;
import com.demo.dto.request.UserCriteria;
import com.demo.dto.utils.PagingReq;
import com.demo.dto.utils.ResponseUtils;
import com.demo.entity.Team;
import com.demo.entity.User;
import com.demo.fake_data.UserInfoGenerator;
import com.demo.repository.TeamRepository;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserResource {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @PostMapping("")
    public ResponseEntity<?> getUsers(@RequestBody(required = false) UserCriteria userCriteria, @Validated PagingReq pagingReq) {
        return ResponseUtils.ok(userService.getUsers(userCriteria, pagingReq));
    }

    @PostMapping("/upsert")
    public ResponseEntity<?> upsertUser(@RequestBody @Validated UpsertUserReq upsertUserReq) {
        userService.upsertUser(upsertUserReq);
        return ResponseUtils.ok();
    }

    @DeleteMapping("/remove-users")
    public ResponseEntity<?> removeUsers(@RequestBody RemoveUsersReq removeUsersReq) {
        userService.removeUsers(removeUsersReq);
        return ResponseUtils.ok();
    }

    @PostMapping("/export-excel")
    public ResponseEntity<?> exportDataUsers(@RequestBody(required = false) UserCriteria userCriteria) {
        return ResponseUtils.ok(userService.exportDataUsers(userCriteria));
    }


    @GetMapping("/fake-data")
    public ResponseEntity<?> fakeData() throws IOException {
        List<User> users = new ArrayList<>();

        LocalDate start = LocalDate.of(1989, 1, 1);
        LocalDate end = LocalDate.of(2004, 1, 1);

        for (int i = 0; i < 300; i++) {
            String fullName = UserInfoGenerator.generateFullName();
            users.add(new User(
                    null,
                    fullName,
                    fullName.replaceAll(" ", "").replaceAll("[^\\x20-\\x7e]", "") + UserInfoGenerator.generateRandomEmail(),
                    Date.from(UserInfoGenerator.between(start, end).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    UserInfoGenerator.generateRandomGender(),
                    UserInfoGenerator.generateRandomPhoneNumber(),
                    UserInfoGenerator.generateRandomAddress(),
                    generateRandomTeam(),
                    UserInfoGenerator.generateRandomStatus()
            ));
        }
        userRepository.saveAll(users);
        return ResponseUtils.ok(users);
    }

    private Team generateRandomTeam() {
        List<Team> teams = teamRepository.findAll();
        Random random = new Random();
        return teams.get(random.nextInt(teams.size()));
    }
}
