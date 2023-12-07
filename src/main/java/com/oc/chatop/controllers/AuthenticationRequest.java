package com.oc.chatop.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//ToDo: reevaluate this file's location relevance
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    //ToDo: check the naming according to the project's requirements
    private String email;
    String password;
}
