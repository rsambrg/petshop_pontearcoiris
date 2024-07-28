package com.projeto.petshop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.model.Produto;
import com.projeto.petshop.service.ProdutoService;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String getAllProduto(Model model) {
        List<Produto> produtos = produtoService.getAllProduto();
        model.addAttribute("produtos", produtos);
        return "produto/list";
    }

    @GetMapping("/{id}")
    public String getProdutoDetails(@PathVariable Long id, Model model) {
        Produto produto = produtoService.getProdutoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        model.addAttribute("produto", produto);
        return "produto/detalhes";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/form";
    }

    @GetMapping("editar/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoService.getProdutoById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido" + id));
        model.addAttribute("produto", produto);
        return "produto/form";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto,
            @RequestParam("imagem") MultipartFile imagem) {
        try {
            produtoService.savProduto(produto, imagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/produto";
    }

    @GetMapping("deletar/{id}")
    public String deleteProduto(@PathVariable("id") Long id) {
        produtoService.deleteProdutoById(id);
        produtoService.deleteProduto(id);
        return "redirect:/produto";
    }

    @GetMapping("/catalogo")
    public String listarProdutos(Model model,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precoMin", required = false) Double precoMin,
            @RequestParam(value = "precoMax", required = false) Double precoMax) {
        List<Produto> produtos;
        if (query != null && !query.isEmpty()) {
            produtos = produtoService.buscarProdutosComFiltros(query, categoria, precoMin, precoMax);
        } else {
            produtos = produtoService.findAllProdutos();
        }
        model.addAttribute("produtos", produtos);
        return "produto/catalogo";
    }

    @GetMapping("/detalhes/{id}")
    public String mostrarDetalhes(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.findProdutoById(id));
        return "produto/detalhes";
    }

}
