package com.oc.chatop.services;

import com.oc.chatop.dto.MessageDTO;
import com.oc.chatop.exceptions.RentalNotFoundException;
import com.oc.chatop.exceptions.UserNotFoundException;
import com.oc.chatop.models.Message;
import com.oc.chatop.models.Rental;
import com.oc.chatop.models.User;
import com.oc.chatop.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RentalService rentalService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService, RentalService rentalService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    /*@PostConstruct
    public void initializeMessages() {
        if (messageRepository.count() == 0) {
            Optional<User> user1 = userService.getUserByEmail("user1@example.com");
            Optional<User> user2 = userService.getUserByEmail("user2@example.com");
            Optional<User> user3 = userService.getUserByEmail("user3@example.com");

            //ToDo: Improve to find Rental fixtures dynamically like it's done to find User fixtures
            Rental rental1 = rentalService.getRentalEntityById(1);
            Rental rental2 = rentalService.getRentalEntityById(2);
            Rental rental3 = rentalService.getRentalEntityById(3);

            Message message1 = new Message(rental1, user2, "Interested in renting this property");
            Message message2 = new Message(rental2, user3, "When can I schedule a visit?");
            Message message3 = new Message(rental3, user1, "Is the property pet-friendly?");

            messageRepository.saveAll(List.of(message1, message2, message3));
        }
    }*/

    @PostConstruct
    public void initializeMessages() {
        if (messageRepository.count() == 0) {
            Optional<User> user1 = userService.getUserByEmail("user1@example.com");
            Optional<User> user2 = userService.getUserByEmail("user2@example.com");
            Optional<User> user3 = userService.getUserByEmail("user3@example.com");

            user1.ifPresent(u -> {
                List<Rental> user1Rentals = rentalService.getRentalsByOwnerId(u.getId());
                user1Rentals.stream().findFirst().ifPresent(rental1 ->
                        messageRepository.save(new Message(rental1, user2, "Interested in renting this property")));
            });

            user2.ifPresent(u -> {
                List<Rental> user2Rentals = rentalService.getRentalsByOwnerId(u.getId());
                user2Rentals.stream().findFirst().ifPresent(rental2 ->
                        messageRepository.save(new Message(rental2, user3, "When can I schedule a visit?")));
            });

            user3.ifPresent(u -> {
                List<Rental> user3Rentals = rentalService.getRentalsByOwnerId(u.getId());
                user3Rentals.stream().findFirst().ifPresent(rental3 ->
                        messageRepository.save(new Message(rental3, user1, "Is the property pet-friendly?")));
            });
        }
    }


    public MessageDTO createMessage(String messageText, Integer userId, Integer rentalId) {
        Optional<User> user = userService.getUserEntityById(userId);
        Rental rental = rentalService.getRentalEntityById(rentalId);

        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        if (rental == null) {
            throw new RentalNotFoundException("Rental not found with ID: " + rentalId);
        }

        Message newMessage = new Message(rental, user, messageText);
        Message savedMessage = messageRepository.save(newMessage);

        return convertToDTO(savedMessage);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setRentalId(message.getRental().getId());
        messageDTO.setUserId(message.getUser().getId());
        messageDTO.setMessage(message.getMessage());

        return messageDTO;
    }
}
