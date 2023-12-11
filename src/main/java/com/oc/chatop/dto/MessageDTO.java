package com.oc.chatop.dto;

import lombok.Data;

@Data
public class MessageDTO implements ApiResponseDTO {
    private Integer id;
    private String message;
    private Integer userId;
    private Integer rentalId;
}
