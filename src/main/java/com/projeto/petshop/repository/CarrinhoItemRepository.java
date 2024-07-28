package com.projeto.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.petshop.model.Carrinho;
import com.projeto.petshop.model.CarrinhoItem;

@Repository
public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {
    List<CarrinhoItem> findByCarrinhoAndStatus(Carrinho carrinho, String status);
    void deleteByProdutoId(Long produtoId);
}
