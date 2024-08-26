package com.PolKevich.ITBcamp0824.service;

import com.PolKevich.ITBcamp0824.dto.*;
import com.PolKevich.ITBcamp0824.mappers.AppMapper;
import com.PolKevich.ITBcamp0824.model.Project;
import com.PolKevich.ITBcamp0824.model.User;
import com.PolKevich.ITBcamp0824.repository.ProjectRepository;
import com.PolKevich.ITBcamp0824.repository.UserRepository;
import com.PolKevich.ITBcamp0824.service.impl.ITBcapmServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ITBcapmServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AppMapper appMapper;

    @InjectMocks
    private ITBcapmServiceImpl itBcapmService;

    @Test
    void testCreateUser() {

        UserRequest userRequest = new UserRequest();
        userRequest.setLastname("Sergeev");
        userRequest.setFirstname("Sergei");
        userRequest.setPatronymic("Sergeevich");
        userRequest.setEmail("Serega@mail.com");
        userRequest.setPost("Project Manager");

        User mappedUser = new User();
        mappedUser.setLastname("Sergeev");
        mappedUser.setFirstname("Sergei");
        mappedUser.setPatronymic("Sergeevich");
        mappedUser.setEmail("Serega@mail.com");
        mappedUser.setPost("Project Manager");

        when(appMapper.convertRequest(userRequest)).thenReturn(mappedUser);
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);

        User createdUser = itBcapmService.create(userRequest);

        assertEquals("Sergeev", createdUser.getLastname());
        assertEquals("Serega@mail.com", createdUser.getEmail());


        verify(appMapper, times(1)).convertRequest(userRequest);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void testCreateProject() {

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectName("ITBCamp in java");
        projectRequest.setProjectDescription("This is a project with a three-level development architecture.");

        Project mappedProject = new Project();
        mappedProject.setProjectName("ITBCamp in java");
        mappedProject.setProjectDescription("This is a project with a three-level development architecture.");

        when(appMapper.convertRequest(projectRequest)).thenReturn(mappedProject);
        when(projectRepository.save(any(Project.class))).thenReturn(mappedProject);

        Project createdProject = itBcapmService.create(projectRequest);

        assertEquals("ITBCamp in java", createdProject.getProjectName());
        assertEquals("This is a project with a three-level development architecture.", createdProject.getProjectDescription());

        verify(appMapper, times(1)).convertRequest(projectRequest);
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @SneakyThrows
    @Test
    void testAddUserToProject() {

        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectName("Test Project");

        User user = new User();
        user.setUserId(userId);
        user.setFirstname("Test user");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        itBcapmService.addUserToProject(projectId, userId);

        assertTrue(user.getProjects().contains(project));

        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user); // сохранение пользователя с обновлённым списком проектов
    }

    @Test
    void testFindAllProjectsAndUsers() {

        Pageable pageable = PageRequest.of(0, 10);

        Project project = new Project();
        project.setProjectId(1L);
        project.setProjectName("ITBCamp in java");
        project.setProjectDescription("This is a project with a three-level development architecture.");

        User user = new User();
        user.setUserId(2L);
        user.setLastname("Ivanov");
        user.setFirstname("Ivan");
        user.setPatronymic("Ivanovich");


        project.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(projectRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(project)));

        UserResponse userResponse = new UserResponse("Ivanov Ivan Ivanovich");
        when(appMapper.userToUserResponse(user)).thenReturn(userResponse);


        Page<ProjectResponse> result = itBcapmService.findAllProjectsAndUsers(pageable);


        assertEquals(1, result.getTotalElements());

        ProjectResponse returnedProject = result.getContent().get(0);

        assertEquals("ITBCamp in java", returnedProject.getProjectName());
        assertEquals(1, returnedProject.getUsers().size());


        UserResponse returnedUserResponse = appMapper.userToUserResponse(user);


        assertEquals("Ivanov Ivan Ivanovich", returnedUserResponse.getFullname());


        verify(projectRepository, times(1)).findAll(pageable);
        verify(appMapper, times(1)).userToUserResponse(user);
    }
}
