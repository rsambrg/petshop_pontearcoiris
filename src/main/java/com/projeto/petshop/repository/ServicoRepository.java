package com.projeto.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Object>{
    
}
