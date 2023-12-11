package com.oc.chatop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserResponseDTO implements ApiResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
