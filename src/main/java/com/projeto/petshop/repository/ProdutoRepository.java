package com.projeto.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Produto;

@Repository
public interface  ProdutoRepository extends JpaRepository<Produto, Object> {
    List<Produto> findByNomeContainingIgnoreCaseAndCategoriaContainingIgnoreCaseAndPrecoBetween(String nome, String categoria, Double precoMin, Double precoMax);
}
