package com.paulathamyres.petshop.appointment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Override
    @EntityGraph(attributePaths = "customer")
    List<Appointment> findAll();

    @Override
    @EntityGraph(attributePaths = "customer")
    Optional<Appointment> findById(Long id);
}
