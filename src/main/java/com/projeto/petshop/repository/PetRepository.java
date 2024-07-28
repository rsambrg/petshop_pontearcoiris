package com.projeto.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUserEmail(String email);
}
