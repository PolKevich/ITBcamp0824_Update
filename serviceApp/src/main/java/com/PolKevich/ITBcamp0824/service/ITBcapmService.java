package com.PolKevich.ITBcamp0824.service;

import com.PolKevich.ITBcamp0824.dto.ProjectRequest;
import com.PolKevich.ITBcamp0824.dto.ProjectResponse;
import com.PolKevich.ITBcamp0824.dto.UserRequest;
import com.PolKevich.ITBcamp0824.model.Project;
import com.PolKevich.ITBcamp0824.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITBcapmService {

    User create(UserRequest request);

    Project create(ProjectRequest request);

    String addUserToProject(Long userId, Long projectId) ;

    Page<ProjectResponse> findAllProjectsAndUsers(Pageable pageable);
}
