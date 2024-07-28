package com.projeto.petshop.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.model.Pet;
import com.projeto.petshop.repository.PetRepository;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public List<Pet> getAllPet() {
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Pet savPet(Pet pet, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(filename).normalize().toAbsolutePath();
            file.transferTo(path.toFile());
            pet.setProfileImagePath(filename);
        }

        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    public void deleteAdm(Long id) {
        petRepository.deleteById(id);
    }

    public List<Pet> getPetsByUserEmail(String userEmail) {
        return petRepository.findByUserEmail(userEmail);
    }

    public List<Pet> getAllPetByUser(String userEmail) {
        return petRepository.findByUserEmail(userEmail);
    }

    public Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet não encontrado"));
    }

    public Pet updatePet(Pet pet, MultipartFile file) throws IOException {
        Pet existingPet = petRepository.findById(pet.getId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        existingPet.setNome(pet.getNome());
        existingPet.setEspecie(pet.getEspecie());
        existingPet.setRaca(pet.getRaca());
        existingPet.setIdade(pet.getIdade());
        existingPet.setUserEmail(pet.getUserEmail());
        existingPet.setBio(pet.getBio());
        existingPet.setTemperamento(pet.getTemperamento());

        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(filename).normalize().toAbsolutePath();
            file.transferTo(path.toFile());
            existingPet.setProfileImagePath(filename);
        }

        if (pet.getAgendamentos() != null) {
            existingPet.getAgendamentos().clear();
            existingPet.getAgendamentos().addAll(pet.getAgendamentos());
        }

        return petRepository.save(existingPet);
    }

}
