package com.oc.chatop.dto;

import lombok.Data;

@Data
public class RentalDTO implements ApiResponseDTO {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private byte[] picture;
    private String description;
    private Integer ownerId;
}
