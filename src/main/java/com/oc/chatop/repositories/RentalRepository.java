package com.oc.chatop.repositories;

import com.oc.chatop.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findAll();
    Optional<Rental> findById(Integer id);
}
