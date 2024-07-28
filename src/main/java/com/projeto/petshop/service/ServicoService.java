package com.projeto.petshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.projeto.petshop.model.Servico;
import com.projeto.petshop.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> getAllServico(){
        return servicoRepository.findAll();
    }

    public Optional<Servico> getServicoById(Long id){
        return servicoRepository.findById(id);
    }

    public Servico savServico(Servico servico){
        return servicoRepository.save(servico);
    }

    public void deleteServico(Long id){
        servicoRepository.deleteById(id);
    }

    public List<Servico> findAllServicos() {
        return servicoRepository.findAll();
    }


}
