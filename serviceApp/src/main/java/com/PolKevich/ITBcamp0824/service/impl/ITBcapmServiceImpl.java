package com.PolKevich.ITBcamp0824.service.impl;

import com.PolKevich.ITBcamp0824.dto.*;
import com.PolKevich.ITBcamp0824.mappers.AppMapper;
import com.PolKevich.ITBcamp0824.model.Project;
import com.PolKevich.ITBcamp0824.model.User;
import com.PolKevich.ITBcamp0824.repository.ProjectRepository;
import com.PolKevich.ITBcamp0824.repository.UserRepository;
import com.PolKevich.ITBcamp0824.service.ITBcapmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@Service
@RequiredArgsConstructor
public class ITBcapmServiceImpl implements ITBcapmService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AppMapper appMapper;


    @Override
    @Transactional
    public User create(UserRequest request) {

        log.info("Create new user: {}", request.toString());

        User createUser = appMapper.convertRequest(request);

        User createdUser = userRepository.save(createUser);

        log.info("User create successful {}", createdUser);

        return createdUser;
    }

    @Override
    @Transactional
    public Project create(ProjectRequest request) {

        log.info("Create new project: {}", request.toString());

        Project createProject = appMapper.convertRequest(request);

        Project createdProject = projectRepository.save(createProject);

        log.info("Project create successful {}", createdProject);

        return createdProject;
    }

    @Override
    @Transactional
    public String addUserToProject(Long projectId, Long userId) {

        log.info("Adding user with ID {} to project with ID {}", projectId, userId);

        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            log.warn("Project with ID {} not found", projectId);
            return "Project not found";
        }

        Project project = optionalProject.get();
        log.info("Project found: {}", project);

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            return "User not found";
        }

        User user = optionalUser.get();
        log.info("User found: {}", user);

        user.getProjects().add(project);
        userRepository.save(user);

        log.info("User with ID {} added to project with ID {}", userId, projectId);

        return "User added to project successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> findAllProjectsAndUsers(Pageable pageable) {

        log.info("Request all projects with users");

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("projectName").ascending());

        Page<Project> projectPage = projectRepository.findAll(sortedPageable);

        List<ProjectResponse> projectResponses = projectPage.stream()
                .map(project -> {
                    List<UserResponse> userResponses = appMapper.usersToUserResponses(new ArrayList<>(project.getUsers()));
                    return appMapper.projectToProjectResponse(project, userResponses);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(projectResponses, pageable, projectPage.getTotalElements());
    }


}
