package com.projeto.petshop.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.petshop.model.Carrinho;
import com.projeto.petshop.model.CarrinhoItem;
import com.projeto.petshop.model.User;
import com.projeto.petshop.service.CarrinhoService;
import com.projeto.petshop.service.UserService;


@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {
    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getCarrinho(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Carrinho carrinho = carrinhoService.getCarrinhoByUser(user);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        carrinho.getItems().forEach(item -> item.setFormattedTotal(decimalFormat.format(item.getProduto().getPreco() * item.getQuantidade())));


        model.addAttribute("carrinho", carrinho);
        model.addAttribute("total", carrinhoService.getTotalValue(carrinho));
        return "carrinho/view";
    }

    @PostMapping("/add")
    public String addItemToCarrinho(@RequestParam Long produtoId, @RequestParam int quantidade, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        carrinhoService.addItemToCarrinho(user, produtoId, quantidade);
        return "redirect:/carrinho";
    }

    @PostMapping("/remove")
    public String removeItemFromCarrinho(@RequestParam Long itemId) {
        carrinhoService.removeItemFromCarrinho(itemId);
        return "redirect:/carrinho";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        carrinhoService.finalizarCarrinho(user);
        return "redirect:/carrinho/minhas-compras";
    }

    @GetMapping("/minhas-compras")
    public String getMinhasCompras(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<CarrinhoItem> compras = carrinhoService.getFinalizadosByUser(user);
        model.addAttribute("compras", compras);
        return "carrinho/minhas-compras";
    }

    

}