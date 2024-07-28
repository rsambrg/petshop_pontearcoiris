package com.projeto.petshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.repository.CarrinhoItemRepository;
import com.projeto.petshop.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.projeto.petshop.model.Produto;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public List<Produto> getAllProduto(){
        return produtoRepository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id){
        return produtoRepository.findById(id);
    }

    public Produto savProduto(Produto produto, MultipartFile imagem) throws IOException {
        if (imagem != null && !imagem.isEmpty()) {
            String filename = imagem.getOriginalFilename();
            Path destinationFile = Paths.get(uploadDir).resolve(filename).normalize().toAbsolutePath();
            imagem.transferTo(destinationFile.toFile());
            produto.setImagemUrl(filename); 
        }
        return produtoRepository.save(produto);
    }
    
    public void deleteProduto(Long id){
        produtoRepository.deleteById(id);
    }

    @Transactional
    public void deleteProdutoById(Long produtoid){
        carrinhoItemRepository.deleteByProdutoId(produtoid);
        produtoRepository.deleteById(produtoid);
    }


    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    public Produto findProdutoById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<Produto> getRandomProdutos(int count) {
        List<Produto> produtos = produtoRepository.findAll();
        Collections.shuffle(produtos, new Random());
        return produtos.stream().limit(count).collect(Collectors.toList());
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public List<Produto> buscarProdutosComFiltros(String nome, String categoria, Double precoMin, Double precoMax) {
        // Se precoMin e precoMax forem null, defina valores padrão
        precoMin = (precoMin != null) ? precoMin : 0.0;
        precoMax = (precoMax != null) ? precoMax : Double.MAX_VALUE;
        return produtoRepository.findByNomeContainingIgnoreCaseAndCategoriaContainingIgnoreCaseAndPrecoBetween(
                nome == null ? "" : nome, categoria == null ? "" : categoria, precoMin, precoMax);
    }
}
