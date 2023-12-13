package com.oc.chatop.dto;

import com.oc.chatop.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeResponseDTO implements ApiResponseDTO {

    private Integer id;
    private String name;
    private String email;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp updated_at;

    public MeResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = user.getCreatedAt();
        this.updated_at = user.getUpdatedAt();
    }
}
