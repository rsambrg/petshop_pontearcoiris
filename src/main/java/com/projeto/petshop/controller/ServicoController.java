package com.projeto.petshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Servico;
import com.projeto.petshop.service.AgendamentoService;
import com.projeto.petshop.service.ServicoService;

@Controller
@RequestMapping("/servico")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public String getAllServico(Model model){
        List<Servico> servicos = servicoService.getAllServico();
        model.addAttribute("servicos", servicos);
        return "servico/list";
    }

    @GetMapping("/catalogo")
    public String getAlllServicos(Model model) {
        List<Servico> servicos = servicoService.getAllServico();
        model.addAttribute("servicos", servicos);
        return "servico/catalogo";
    }

    @GetMapping("/novo")
    public String showForm(Model model){
        model.addAttribute("servico", new Servico());
        return "servico/form";
    }

    @GetMapping("editar/{id}")
    public String showUpdateForm(@PathVariable("id")Long id,Model model){
        Servico servico = servicoService.getServicoById(id).orElseThrow(()-> new IllegalArgumentException("Id invalido "+ id));
        model.addAttribute("servico", servico);
        return "servico/form";
    }

    @PostMapping("/salvar")
    public String salvarServico(Servico servico){
        servicoService.savServico(servico);
        return "redirect:/servico";
    }

    @GetMapping("deletar/{id}")
    public String deleteServico(@PathVariable("id")Long id){
        servicoService.deleteServico(id);
        return "redirect:/servico";
    }

    @GetMapping("/form/{id}")
    public String showAgendamentoForm(@PathVariable("id") Long id, Model model) {
        Servico servico = servicoService.getServicoById(id).orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + id));
        model.addAttribute("servico", servico);
        List<Long> horariosDisponiveis = agendamentoService.getHorariosDisponiveis();
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        Agendamento agendamento = new Agendamento();
        model.addAttribute("agendamento", agendamento);
        return "agendamento/form";
    }
}
