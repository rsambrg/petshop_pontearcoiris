package com.projeto.petshop.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Pet;
import com.projeto.petshop.model.Servico;
import com.projeto.petshop.model.User;
import com.projeto.petshop.service.AgendamentoService;
import com.projeto.petshop.service.PetService;
import com.projeto.petshop.service.ServicoService;
import com.projeto.petshop.service.UserService;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllAgendamento(Model model) {
        List<Agendamento> todosAgendamentos = agendamentoService.getAllAgendamento();
        List<Agendamento> agendamentosNaoConcluidos = todosAgendamentos.stream()
                .filter(agendamento -> "Nao Concluido".equals(agendamento.getStatus()))
                .collect(Collectors.toList());
        List<Agendamento> agendamentosConcluidos = todosAgendamentos.stream()
                .filter(agendamento -> "Concluido".equals(agendamento.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("agendamentosNaoConcluidos", agendamentosNaoConcluidos);
        model.addAttribute("agendamentosConcluidos", agendamentosConcluidos);

        return "agendamento/list";
    }

    @GetMapping("/meus-agendamentos")
    public String getUsersAgendamentos(Model model, Principal principal) {
        String email = principal.getName();
        List<Agendamento> agendamentos = agendamentoService.getUserAgendamentosByStatus(email, "Nao Concluido");
        model.addAttribute("agendamentos", agendamentos);
        return "agendamento/meus-agendamentos";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> getAgendamentoById(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoService.getAgendamentoById(id);
        return agendamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Agendamento createAgendamento(@RequestBody Agendamento agendamento) {
        return agendamentoService.savAgendamento(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> updateAgendamento(@PathVariable Long id, @RequestBody Agendamento agendamento) {
        agendamento.setId(id);
        Agendamento updatedAgendamento = agendamentoService.updateAgendamento(id, agendamento);
        return ResponseEntity.ok(updatedAgendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendamento(@PathVariable("id") Long id, Model model) {
        agendamentoService.deleteAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/novo")
    public String novoAgendamento(Model model, Principal principal) {
        String userEmail = principal.getName();
        List<Servico> servicos = servicoService.getAllServico();
        List<Pet> pets = petService.getAllPetByUser(userEmail); // Ajuste conforme necessário para obter os pets do usuário logado
        List<String> horariosDisponiveis = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("servicos", servicos);
        model.addAttribute("pets", pets);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);

        return "agendamento/form";
    }

    @GetMapping("/novoadm")
    public String novoAgendamentoadm(Model model, Principal principal) {
        String userEmail = principal.getName();
        List<Servico> servicos = servicoService.getAllServico();
        List<Pet> pets = petService.getAllPetByUser(userEmail);
        List<String> horariosDisponiveis = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("servicos", servicos);
        model.addAttribute("pets", pets);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);

        return "agendamento/formadm";
    }

    @GetMapping("editar/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, Principal principal) {
        Agendamento agendamento = agendamentoService.getAgendamentoById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido " + id));
        String userEmail = agendamento.getUserEmail();
        List<Servico> servicos = servicoService.getAllServico();
        List<Pet> pets = petService.getAllPetByUser(userEmail); // Ajuste conforme necessário para obter os pets do usuário logado
        List<String> horariosDisponiveis = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        model.addAttribute("servicos", servicos);
        model.addAttribute("pets", pets);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        
        model.addAttribute("agendamento", agendamento);
        
        return "agendamento/edit-agendamento";
    }

    @PostMapping("/atualizar/{id}")
    public String updateAgendamento(@PathVariable("id") Long id,
            @ModelAttribute("agendamento") Agendamento agendamento, Principal principal, Model model) {
        String userEmail = principal.getName();
        List<Servico> servicos = servicoService.getAllServico();
        List<Pet> pets = petService.getAllPetByUser(userEmail); // Ajuste conforme necessário para obter os pets do usuário logado
        List<String> horariosDisponiveis = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        model.addAttribute("servicos", servicos);
        model.addAttribute("pets", pets);
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);

        agendamento.setUserEmail(principal.getName());
        agendamento.setId(id);
        agendamentoService.updateAgendamento(id, agendamento);

        User user = userService.findByEmail(userEmail);
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (isAdmin) {
            return "redirect:/agendamento";
        } else {
            return "redirect:/agendamento/meus-agendamentos";
        }
    }

    @PostMapping("/salvar")
    public String salvarAgendamento(@ModelAttribute("agendamento") Agendamento agendamento, Principal principal) {
        agendamento.setUserEmail(principal.getName());
        agendamentoService.savAgendamento(agendamento);
        return "redirect:/agendamento/meus-agendamentos";
    }


    @GetMapping("deletar/{id}")
    public String deleteAgendamento(@PathVariable("id") Long id) {
        agendamentoService.deleteAgendamento(id);
        return "redirect:/agendamento/meus-agendamentos";
    }

    @GetMapping("deleteadm/{id}")
    public String deleteadm(@PathVariable("id") Long id) {
        agendamentoService.deleteAgendamento(id);
        return "redirect:/agendamento";
    }

    @GetMapping("/historico")
    public String getAgendamentosConcluidos(Model model, Principal principal) {
        String userEmail = principal.getName();
        List<Agendamento> agendamentosConcluidos = agendamentoService.getUserAgendamentosByStatus(userEmail, "Concluido");
        model.addAttribute("agendamentosConcluidos", agendamentosConcluidos);
        return "agendamento/historico";
    }

    @PostMapping("/concluir/{id}")
    public String concluirAgendamento(@PathVariable("id") Long id) {
        agendamentoService.atualizarStatusParaConcluido(id);
        return "redirect:/agendamento/meus-agendamentos";
    }

    @PostMapping("/concluiradm/{id}")
    public String concluirAgendamentoadm(@PathVariable("id") Long id) {
        agendamentoService.atualizarStatusParaConcluido(id);
        return "redirect:/agendamento";
    }

}
