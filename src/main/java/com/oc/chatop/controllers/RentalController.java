package com.oc.chatop.controllers;

import com.oc.chatop.dto.RentalDTO;
import com.oc.chatop.models.User;
import com.oc.chatop.services.RentalService;
import com.oc.chatop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;

    @Autowired
    public RentalController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentalsDTO() {
        Map<String, List<RentalDTO>> response = rentalService.getAllRentalsDTO();
        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rentalDTO = rentalService.getRentalDTOById(id);

        if (rentalDTO != null) {
            return new ResponseEntity<>(rentalDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam String name,
            @RequestParam Double surface,
            @RequestParam Double price,
            @RequestPart MultipartFile picture,
            @RequestParam String description) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String loggedInUsername = authentication.getName();
        Optional<User> owner = userService.getUserByEmail(loggedInUsername);

        if (owner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        rentalService.createRental(name, surface, price, picture, description, owner);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental created!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam Double surface,
            @RequestParam Double price,
            @RequestParam String description) {

        rentalService.updateRental(id, name, surface, price, description);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental updated!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
