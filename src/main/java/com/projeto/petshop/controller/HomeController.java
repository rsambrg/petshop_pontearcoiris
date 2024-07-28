package com.projeto.petshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.petshop.model.Produto;
import com.projeto.petshop.service.ProdutoService;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/")
    public String home(Model model){
        List<Produto> produtos = produtoService.getRandomProdutos(4); // Adicione este método no serviço
        model.addAttribute("produtos", produtos);
        return "home";
    }

    @GetMapping("painel")
    public String painel(){
        return "painel";
    }

    @GetMapping("acesso-restrito")
    public String acessorestrito(){
        return "acesso-restrito";
    }

    @GetMapping("veterinarios")
    public String veterinarios(){
        return "veterinarios";
    }

    @GetMapping("banhotosa")
    public String banhotosa(){
        return "banhotosa";
    }

    @GetMapping("sobre")
    public String sobre(){
        return "sobre";
    }

    @GetMapping("trabalheconosco")
    public String trabalheconosco(){
        return "trabalheconosco";
    }

    @GetMapping("contato")
    public String contato(){
        return "contato";
    }

    @GetMapping("privacidade")
    public String privacidade(){
        return "privacidade";
    }
}
