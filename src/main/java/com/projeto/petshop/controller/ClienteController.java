package com.projeto.petshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.petshop.model.Cliente;
import com.projeto.petshop.service.ClienteService;


@Controller
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getAllCliente(Model model){
        List<Cliente> clientes = clienteService.getAllCliente();
        model.addAttribute("clientes", clientes);
        return "cliente/list";

    }

    @GetMapping("/novo")
    public String showForm (Model model){
        model.addAttribute("cliente", new Cliente());
        return "cliente/form";
    }

    @GetMapping("editar/{id}")
    public String showUpdateForm(@PathVariable("id")Long id, Model model){
        Cliente cliente = clienteService.getClienteById(id).orElseThrow(()-> new IllegalArgumentException("Id invalido "+ id));
        model.addAttribute("cliente", cliente);
        return "cliente/form";
    }

    @PostMapping("/salvar")
    public String salvarCliente(Cliente cliente){
        clienteService.savCliente(cliente);
        return "redirect:/login";
    }

    @GetMapping("deletar/{id}")
    public String deleteCliente(@PathVariable("id")Long id){
        clienteService.deleteCliente(id);
        return "redirect:/cliente";
    }
}
