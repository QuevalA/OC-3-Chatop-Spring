package com.oc.chatop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class RentalDTO implements ApiResponseDTO {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Integer owner_id;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp updated_at;
}
