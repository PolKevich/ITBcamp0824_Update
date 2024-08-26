package com.PolKevich.ITBcamp0824.controller;

import com.PolKevich.ITBcamp0824.dto.ProjectRequest;
import com.PolKevich.ITBcamp0824.dto.ProjectResponse;
import com.PolKevich.ITBcamp0824.dto.UserRequest;
import com.PolKevich.ITBcamp0824.model.Project;
import com.PolKevich.ITBcamp0824.model.User;
import com.PolKevich.ITBcamp0824.service.ITBcapmService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2

public class ITBcampController {

    @Autowired
private ITBcapmService itBcapmService;

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequest request) {

        User createdUser = itBcapmService.create(request);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/project")
    public ResponseEntity<Project> addProject(@Valid @RequestBody ProjectRequest request) {

        Project createdProject = itBcapmService.create(request);

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PostMapping("/projectid/{projectId}/userid/{userId}")
    public ResponseEntity<String> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {

        String result = itBcapmService.addUserToProject(projectId, userId);

        if ("Project not found".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        else if ("User not found".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        else if ("User added to project successfully".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getproject")
    public ResponseEntity<Page<ProjectResponse>> findAllProjectsAndUsers(Pageable pageable) {

        Page<ProjectResponse> projectsWithUsers = itBcapmService.findAllProjectsAndUsers(pageable);

        if (projectsWithUsers.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        return new ResponseEntity<>(projectsWithUsers, HttpStatus.OK);

    }

}
