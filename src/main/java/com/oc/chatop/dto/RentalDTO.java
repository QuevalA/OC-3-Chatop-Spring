package com.oc.chatop.dto;

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
    private Timestamp created_at;
    private Timestamp updated_at;
}
