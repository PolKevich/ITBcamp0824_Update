package com.PolKevich.ITBcamp0824.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectResponse {

    private String projectName;

    private String projectDescription;

    private Set<UserResponse> users;

}
