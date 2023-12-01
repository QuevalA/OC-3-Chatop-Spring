package com.oc.chatop.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oc.chatop.dto.MessageDTO;
import com.oc.chatop.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody CreateMessageRequest request) {
        try {
            MessageDTO createdMessage = messageService.createMessage(
                    request.getMessage(),
                    request.getUserId(),
                    request.getRentalId()
            );

            return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static class CreateMessageRequest {
        @JsonProperty("message")
        private String message;

        @JsonProperty("user_id")
        private Integer userId;

        @JsonProperty("rental_id")
        private Integer rentalId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getRentalId() {
            return rentalId;
        }

        public void setRentalId(Integer rentalId) {
            this.rentalId = rentalId;
        }
    }
}
