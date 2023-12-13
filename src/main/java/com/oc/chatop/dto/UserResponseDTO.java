package com.oc.chatop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserResponseDTO implements ApiResponseDTO {
    private Integer id;
    private String name;
    private String email;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp updated_at;
}
