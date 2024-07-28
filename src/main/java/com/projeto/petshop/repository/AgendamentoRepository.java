package com.projeto.petshop.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Agendamento;


@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Object> {
    List<Agendamento> findByUserEmail(String email);
    List<Agendamento> findByData(LocalDate data);
    List<Agendamento> findByUserEmailAndStatus(String userEmail, String status);

}
