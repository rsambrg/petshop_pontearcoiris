package com.projeto.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Carrinho;
import com.projeto.petshop.model.User;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Carrinho findByUser(User user);
}