package com.oc.chatop.repositories;

import com.oc.chatop.models.Rental;
import com.oc.chatop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findById(Integer id);
}
