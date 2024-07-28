package com.projeto.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Object> {
    Role findByName(String name);
}
