package com.oc.chatop.controllers;

import com.oc.chatop.dto.RentalDTO;
import com.oc.chatop.models.User;
import com.oc.chatop.services.RentalService;
import com.oc.chatop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping
    public List<RentalDTO> getAllRentals() {
        return rentalService.getAllRentalsDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalDTO rentalDTO = rentalService.getRentalDTOById(id);

        if (rentalDTO != null) {
            return new ResponseEntity<>(rentalDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<RentalDTO> createRental(
            @RequestParam String name,
            @RequestParam Double surface,
            @RequestParam Double price,
            @RequestParam String picture,
            @RequestParam String description,
            @RequestParam Integer ownerId) {

        User owner = userService.getUserEntityById(ownerId);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        RentalDTO rentalDTO = rentalService.createRental(
                name, surface, price, picture, description, owner);

        return new ResponseEntity<>(rentalDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam Double surface,
            @RequestParam Double price,
            @RequestParam String description) {

        return new ResponseEntity<>(rentalService.updateRental
                (id, name, surface, price, description), HttpStatus.OK);
    }
}