package com.oc.chatop.services;

import com.oc.chatop.dto.RentalDTO;
import com.oc.chatop.exceptions.RentalNotFoundException;
import com.oc.chatop.models.Rental;
import com.oc.chatop.models.User;
import com.oc.chatop.repositories.RentalRepository;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

    private String getPlaceholderImageUrl() {
        Resource resource = new ClassPathResource("images/placeholder-rental-property-1.txt");

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(resource.getURI()));
            return new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read placeholder image file.", e);
        }
    }

    @PostConstruct
    public void initializeRentals() {
        if (rentalRepository.count() == 0) {
            Optional<User> user1 = userService.getUserByEmail("user1@example.com");
            Optional<User> user2 = userService.getUserByEmail("user2@example.com");
            Optional<User> user3 = userService.getUserByEmail("user3@example.com");

            String placeholderImageUrl = getPlaceholderImageUrl();

            Rental rental1 = new Rental("House One", 150.0, 1000.0, placeholderImageUrl, "Spacious house with a garden", user1);
            Rental rental2 = new Rental("Apartment Two", 80.0, 800.0, placeholderImageUrl, "Modern apartment in the city center", user2);
            Rental rental3 = new Rental("Cottage Three", 120.0, 1200.0, placeholderImageUrl, "Charming cottage near the mountains", user3);

            rentalRepository.saveAll(List.of(rental1, rental2, rental3));
        }
    }

    public Map<String, List<RentalDTO>> getAllRentalsDTO() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalDTO> rentalDTOs = rentals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentalDTOs);

        return response;
    }

    public Rental getRentalEntityById(Integer id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public RentalDTO getRentalDTOById(Integer id) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);

        return optionalRental.map(this::convertToDTO).orElse(null);
    }

    public RentalDTO createRental(String name, Double surface, Double price, MultipartFile pictureFile, String description, Optional<User> owner) {
        try {
            String base64Image = encodeImageToBase64(pictureFile.getBytes());
            String dataUrl = "data:image/jpeg;base64," + base64Image;
            Rental newRental = new Rental(name, surface, price, dataUrl, description, owner);
            Rental savedRental = rentalRepository.save(newRental);

            return convertToDTO(savedRental);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to encode image to Base64.", e);
        }
    }

    private String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public RentalDTO updateRental(
            Integer id,
            String name,
            Double surface,
            Double price,
            String description) {

        Optional<Rental> optionalRental = rentalRepository.findById(id);

        Rental rental = optionalRental.orElseThrow(() -> new RentalNotFoundException("Rental not found"));

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        Rental updatedRental = rentalRepository.save(rental);

        return convertToDTO(updatedRental);
    }

    public List<Rental> getRentalsByOwnerId(Integer ownerId) {
        return rentalRepository.findByOwnerId(ownerId);
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setOwner_id(rental.getOwner().getId());
        rentalDTO.setCreated_at(rental.getCreatedAt());
        rentalDTO.setUpdated_at(rental.getUpdatedAt());
        rentalDTO.setPicture(rental.getPicture());

        return rentalDTO;
    }
}
