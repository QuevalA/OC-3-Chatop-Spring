package com.oc.chatop.services;

import com.oc.chatop.dto.RentalDTO;
import com.oc.chatop.exceptions.RentalNotFoundException;
import com.oc.chatop.models.Rental;
import com.oc.chatop.models.User;
import com.oc.chatop.repositories.RentalRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserService userService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void initializeRentals() {
        if (rentalRepository.count() == 0) {
            Optional<User> user1 = userService.getUserByEmail("user1@example.com");
            Optional<User> user2 = userService.getUserByEmail("user2@example.com");
            Optional<User> user3 = userService.getUserByEmail("user3@example.com");

            byte[] picture1 = loadImageAsBytes("house1.jpg");
            byte[] picture2 = loadImageAsBytes("apartment2.jpg");
            byte[] picture3 = loadImageAsBytes("cottage3.jpg");

            Rental rental1 = new Rental("House One", 150.0, 1000.0, picture1, "Spacious house with a garden", user1);
            Rental rental2 = new Rental("Apartment Two", 80.0, 800.0, picture2, "Modern apartment in the city center", user2);
            Rental rental3 = new Rental("Cottage Three", 120.0, 1200.0, picture3, "Charming cottage near the mountains", user3);

            rentalRepository.saveAll(List.of(rental1, rental2, rental3));
        }
    }

    private byte[] loadImageAsBytes(String filename) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/" + filename)) {
            if (inputStream != null) {
                return inputStream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0]; // Return empty array if the image couldn't be loaded
    }

    public List<RentalDTO> getAllRentalsDTO() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Rental getRentalEntityById(Integer id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public RentalDTO getRentalDTOById(Integer id) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);

        return optionalRental.map(this::convertToDTO).orElse(null);
    }

    public RentalDTO createRental(String name, Double surface, Double price, byte[] picture, String description, User owner) {
        Rental newRental = new Rental(name, surface, price, picture, description, Optional.of(owner));
        Rental savedRental = rentalRepository.save(newRental);
        return convertToDTO(savedRental);
    }

    public RentalDTO updateRental(Integer id, String name, Double surface, Double price, String description) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);

        Rental rental = optionalRental.orElseThrow(() -> new RentalNotFoundException("Rental not found"));

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        Rental updatedRental = rentalRepository.save(rental);

        return convertToDTO(updatedRental);
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setPicture(rental.getPicture());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setOwnerId(rental.getOwner().getId());
        return rentalDTO;
    }
}
