package com.projeto.petshop.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.model.Pet;
import com.projeto.petshop.model.User;
import com.projeto.petshop.service.PetService;
import com.projeto.petshop.service.UserService;

@Controller
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;

    @Value("${upload.dir}")
    private String uploadDir;

    @GetMapping
    public String getAllPet(Model model) {
        List<Pet> pets = petService.getAllPet();
        model.addAttribute("pets", pets);
        return "pet/list";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "pet/form";
    }

    @GetMapping("/meus-pets")
    public String getUsersPet(Model model, Principal principal) {
        String email = principal.getName();
        List<Pet> pets = userService.getUserPets(email);
        model.addAttribute("pets", pets);
        return "pet/meus-pets";
    }

    @GetMapping("editar/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Pet pet = petService.getPetById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido" + id));
        model.addAttribute("pet", pet);
        return "pet/edit-pet";
    }

    @PostMapping("/atualizar/{id}")
    public String updatePet(@PathVariable("id") Long id,
            @ModelAttribute("pet") Pet pet,
            @RequestParam("file") MultipartFile file, Principal principal) {
        pet.setUserEmail(principal.getName());
        try {
            pet.setId(id);
            petService.updatePet(pet, file);
            return "redirect:/pet/detalhes/" + id;
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/pet/editar/" + id;
        }
    }

    @GetMapping("/{id}")
    public String getPetDetails(@PathVariable Long id, Model model) {
        Pet pet = petService.getPetById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet não encontrado"));
        model.addAttribute("pet", pet);
        return "pet/detalhes";
    }

    @GetMapping("/detalhes/{id}")
    public String mostrarDetalhes(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findPetById(id));
        return "pet/detalhes";
    }

    @PostMapping("/salvar")
    public String salvarPet(@ModelAttribute Pet pet,
            @RequestParam("file") MultipartFile file, Principal principal, User user) {
        pet.setUserEmail(principal.getName());
        pet.setIdcliente(user.getId());
        try {
            petService.savPet(pet, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/pet/meus-pets";
    }

    @GetMapping("deletar/{id}")
    public String deletePet(@PathVariable("id") Long id) {
        petService.deletePet(id);
        return "redirect:/pet/meus-pets";
    }

    @GetMapping("deleteadm/{id}")
    public String deleteAdm(@PathVariable("id") Long id) {
        petService.deletePet(id);
        return "redirect:/pet";
    }

}
