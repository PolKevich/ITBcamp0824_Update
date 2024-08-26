package com.PolKevich.ITBcamp0824.mappers;

import com.PolKevich.ITBcamp0824.dto.*;
import com.PolKevich.ITBcamp0824.model.Project;
import com.PolKevich.ITBcamp0824.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppMapper {

    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "patronymic", target = "patronymic")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "post", target = "post")
    User convertRequest(UserRequest userRequest);

    @Mapping(source = "projectName", target = "projectName")
    @Mapping(source = "projectDescription", target = "projectDescription")
    Project convertRequest(ProjectRequest projectRequest);

    @Mapping(target = "fullname", expression = "java(user.getLastname() + \" \" + user.getFirstname() + \" \" + user.getPatronymic())")
    UserResponse userToUserResponse(User user);


    List<UserResponse> usersToUserResponses(List<User> users);

    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "project.projectDescription", target = "projectDescription")
    @Mapping(source = "userResponses", target = "users")
    ProjectResponse projectToProjectResponse(Project project, List<UserResponse> userResponses);

}
