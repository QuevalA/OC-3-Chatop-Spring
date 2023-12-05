package com.oc.chatop.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//ToDo: once feature works, change this file's location for a more logical project structure

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    //ToDo: adapt the naming to the project's requirements
    private String email;
    String password;
}
