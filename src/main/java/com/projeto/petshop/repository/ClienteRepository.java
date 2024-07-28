package com.projeto.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Object> {
    
}
