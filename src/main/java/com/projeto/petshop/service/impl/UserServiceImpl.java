package com.projeto.petshop.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.dto.UserDto;
import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Carrinho;
import com.projeto.petshop.model.Pet;
import com.projeto.petshop.model.Role;
import com.projeto.petshop.model.User;
import com.projeto.petshop.repository.AgendamentoRepository;
import com.projeto.petshop.repository.PetRepository;
import com.projeto.petshop.repository.RoleRepository;
import com.projeto.petshop.repository.UserRepository;
import com.projeto.petshop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Value("${upload.dir}")
    private String uploadDir;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private PetRepository petRepository;
    private AgendamentoRepository agendamentoRepository;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            PetRepository petRepository,
            AgendamentoRepository agendamentoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.petRepository = petRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName() + " " + userDto.getLastname());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Carrinho carrinho = new Carrinho();
        carrinho.setUser(user);
        user.setCarrinho(carrinho);

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole != null) {
            user.setRoles(Arrays.asList(userRole));
        }
        userRepository.save(user);
    }

    public class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Override
    public void promoteToAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Remove o papel ADMIN e adiciona o papel USER se não estiver presente
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (user.getRoles().contains(userRole)) {
            user.getRoles().remove(userRole);
        }

        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }

    public void demoteToUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o papel USER já existe, caso contrário cria o papel
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        // Remove o papel ADMIN e adiciona o papel USER se não estiver presente
        if (user.getRoles().contains(adminRole)) {
            user.getRoles().remove(adminRole);
        }

        if (!user.getRoles().contains(userRole)) {
            user.getRoles().add(userRole);
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(String email, UserDto userDto, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email);
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress() == null ? " " : userDto.getAddress());
        user.setAge(userDto.getAge() == null ? 0 : userDto.getAge());
        user.setGender(userDto.getGender() == null ? " " : userDto.getGender());
        user.setBirthDate(userDto.getBirthDate() == null ? LocalDate.parse(" ") : userDto.getBirthDate());

        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(filename).normalize().toAbsolutePath();
            file.transferTo(path.toFile());
            user.setProfileImagePath(filename);
        }

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return convertEntityToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = convertEntityToDto(user);
            List<Long> roleIds = user.getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());
            userDto.setRole(roleIds);
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Pet> getUserPets(String email) {
        return petRepository.findByUserEmail(email);
    }

    @Override
    public List<Agendamento> getUserAgendamentos(String email) {
        return agendamentoRepository.findByUserEmail(email);
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");

        if (name.length >= 2) {
            userDto.setName(name[0]);
            userDto.setName(String.join(" ", Arrays.copyOfRange(name, 0, name.length)));
        } else {
            userDto.setName(name[0]);
            userDto.setLastname("");
        }
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAddress(user.getAddress());
        userDto.setLastname(user.getLastname());
        userDto.setAge(user.getAge());
        userDto.setGender(user.getGender());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setProfileImagePath(user.getProfileImagePath());

        List<Long> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        userDto.setRole(roleIds);

        return userDto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
