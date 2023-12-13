package com.oc.chatop.controllers;

import com.oc.chatop.models.User;
import com.oc.chatop.services.MessageService;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody CreateMessageRequest request) {
        try {
            // Retrieve the authenticated user's principal from SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Cast the principal to User
            User user = (User) authentication.getPrincipal();
            Integer loggedInUserId = user.getId();

            // Call the service method with the authenticated user's ID
            messageService.createMessage(request.getMessage(), loggedInUserId, request.getRentalId());

            // Create a custom response message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Message sent with success");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static class CreateMessageRequest {
        @JsonProperty("message")
        private String message;

        @JsonProperty("rental_id")
        private Integer rentalId;

        public String getMessage() {
            return message;
        }

        public Integer getRentalId() {
            return rentalId;
        }
    }
}
