package com.projeto.petshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.petshop.model.Carrinho;
import com.projeto.petshop.model.CarrinhoItem;
import com.projeto.petshop.model.Produto;
import com.projeto.petshop.model.User;
import com.projeto.petshop.repository.CarrinhoItemRepository;
import com.projeto.petshop.repository.CarrinhoRepository;
import com.projeto.petshop.repository.ProdutoRepository;
import com.projeto.petshop.repository.UserRepository;

@Service
public class CarrinhoService {
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserRepository userRepository;

    public Carrinho getCarrinhoByUser(User user) {
        Carrinho carrinho = carrinhoRepository.findByUser(user);
        if ( carrinho != null){
            List<CarrinhoItem> itemsAguardando = carrinhoItemRepository.findByCarrinhoAndStatus(carrinho, "aguardando");
            carrinho.setItems(itemsAguardando);
        }
        return carrinhoRepository.findByUser(user);
    }

    public void addItemToCarrinho(User user, Long produtoId, int quantidade) {
        Carrinho carrinho = carrinhoRepository.findByUser(user);
        if (carrinho == null) {
            carrinho = new Carrinho();
            carrinho.setUser(user);
            carrinhoRepository.save(carrinho);
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        CarrinhoItem item = new CarrinhoItem();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setCarrinho(carrinho);
        item.setStatus("aguardando");
        carrinhoItemRepository.save(item);
    }

    public void removeItemFromCarrinho(Long itemId) {
        carrinhoItemRepository.deleteById(itemId);
    }


    public double getTotalValue(Carrinho carrinho) {
        return carrinho.getItems().stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
    }

    public void finalizarCarrinho(User user){
        Carrinho carrinho = carrinhoRepository.findByUser(user);
        if(carrinho!= null){
            List<CarrinhoItem> items = carrinho.getItems();
            for(CarrinhoItem item : items){
                if("aguardando".equals(item.getStatus())){
                    item.setStatus("finalizado");
                    carrinhoItemRepository.save(item);
                }
            }
        }
    }

    public List<CarrinhoItem> getFinalizadosByUser(User user) {
        Carrinho carrinho = carrinhoRepository.findByUser(user);
        return carrinhoItemRepository.findByCarrinhoAndStatus(carrinho, "finalizado");
    }

    public List<CarrinhoItem> getUserCarrinhoAndStatus(Carrinho carrinho, String status){
        return carrinhoItemRepository.findByCarrinhoAndStatus(carrinho, status);
    }

}
