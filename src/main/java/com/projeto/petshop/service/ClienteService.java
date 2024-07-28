package com.projeto.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.petshop.model.Cliente;
import com.projeto.petshop.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> getAllCliente(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id){
        return clienteRepository.findById(id);
    }

    public Cliente savCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id){
        clienteRepository.deleteById(id);
    }

    
}
