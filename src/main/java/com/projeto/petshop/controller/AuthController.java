package com.projeto.petshop.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.petshop.dto.UserDto;
import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Pet;
import com.projeto.petshop.model.Role;
import com.projeto.petshop.model.User;
import com.projeto.petshop.service.AgendamentoService;
import com.projeto.petshop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("index")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        log.info("login page");
        return "login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "cliente/list";
    }

    @GetMapping("/minha-conta")
    public String minhaConta(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        UserDto userDto = userService.findById(user.getId());
        model.addAttribute("user", userDto);

        List<Pet> pets = userService.getUserPets(email);
        model.addAttribute("pets", pets);

        // Busca os agendamentos associados ao usuário pelo email
        List<Agendamento> agendamentos = userService.getUserAgendamentos(email);
        model.addAttribute("agendamentos", agendamentos);

        return "minha-conta";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
        return "redirect:/login";
    }

    @GetMapping("/api/isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(Principal principal) {
        return ResponseEntity.ok(principal != null);
    }

    @GetMapping("/api/role")
    public ResponseEntity<Map<String, String>> getUserRole(Principal principal) {
        Map<String, String> response = new HashMap<>();
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            String role = user.getRoles().stream()
                    .map(Role::getName)
                    .findFirst()
                    .orElse("ROLE_USER");
            response.put("role", role);
        } else {
            response.put("role", "ROLE_ANONYMOUS");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promote-to-admin")
    public String promoteToAdmin(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.promoteToAdmin(id);
            redirectAttributes.addFlashAttribute("message", "Usuário promovido a ADMIN com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao promover usuário");
        }
        return "redirect:/users";
    }

    @PostMapping("/demote-to-user")
    public String demoteToUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.demoteToUser(id);
            redirectAttributes.addFlashAttribute("message", "Usuário rebaixado para USER com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao rebaixar usuário");
        }
        return "redirect:/users";
    }

    @GetMapping("/edit-profile")
    public String showEditProfileForm(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Principal principal,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit-profile";
        }  try {
            userService.updateUser(principal.getName(), userDto,file);
            redirectAttributes.addFlashAttribute("message", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar o perfil.");
        }
        return "redirect:/minha-conta";
    }

    @GetMapping("deletar/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("users/detalhes/{id}")
    public String mostrarDetalhes(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "cliente/detalhes";
    }

}
