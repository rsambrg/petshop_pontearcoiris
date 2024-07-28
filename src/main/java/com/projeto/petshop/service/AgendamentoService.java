package com.projeto.petshop.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Servico;
import com.projeto.petshop.repository.AgendamentoRepository;
import com.projeto.petshop.repository.ServicoRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ServicoRepository servicoRepository;

    public List<Agendamento> getAllAgendamento() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> getAgendamentoById(Long id) {
        return agendamentoRepository.findById(id);
    }

    public List<Agendamento> getAgendamentosByData(LocalDate data) {
        return agendamentoRepository.findByData(data);
    }

    public Agendamento savAgendamento(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento updateAgendamento(Long id, Agendamento agendamento) {
        Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
        if (optionalAgendamento.isPresent()) {
            Agendamento existingAgendamento = optionalAgendamento.get();
            existingAgendamento.setServico(agendamento.getServico());
            existingAgendamento.setData(agendamento.getData());
            existingAgendamento.setHora(agendamento.getHora());
            existingAgendamento.setPet(agendamento.getPet());
            existingAgendamento.setStatus(agendamento.getStatus());
            // Atualize quaisquer outros campos conforme necessário
            return agendamentoRepository.save(existingAgendamento);
        } else {
            // Lance uma exceção ou retorne um valor adequado
            throw new RuntimeException("Agendamento não encontrado com id " + id);
        }
    }

    public void deleteAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public List<Long> getHorariosDisponiveis() {
        return List.of(10L, 14L, 16L);
    }

    public Optional<Servico> getServicoById(Long id) {
        return servicoRepository.findById(id);
    }

    public void atualizarStatusParaConcluido(Long id) {
        Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
        if (optionalAgendamento.isPresent()) {
            Agendamento agendamento = optionalAgendamento.get();
            agendamento.setStatus("Concluido");
            agendamentoRepository.save(agendamento);
        } else {
            throw new RuntimeException("Agendamento não encontrado com id " + id);
        }
    }

    public List<Agendamento> getUserAgendamentosByStatus(String userEmail, String status) {
        return agendamentoRepository.findByUserEmailAndStatus(userEmail, status);
    }

    
}
