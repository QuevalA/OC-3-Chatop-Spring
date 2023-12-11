package com.oc.chatop.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oc.chatop.dto.MessageDTO;
import com.oc.chatop.models.User;
import com.oc.chatop.services.MessageService;
import com.oc.chatop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    /*@PostMapping
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
    }*/

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody CreateMessageRequest request) {
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
            MessageDTO createdMessage = messageService.createMessage(
                    request.getMessage(),
                    loggedInUserId,  // Pass the authenticated user's ID
                    request.getRentalId()
            );

            return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
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

        public void setMessage(String message) {
            this.message = message;
        }


        public Integer getRentalId() {
            return rentalId;
        }

        public void setRentalId(Integer rentalId) {
            this.rentalId = rentalId;
        }
    }
}
