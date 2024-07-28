package com.projeto.petshop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.petshop.dto.UserDto;
import com.projeto.petshop.model.Agendamento;
import com.projeto.petshop.model.Pet;
import com.projeto.petshop.model.User;

public interface UserService {


    void saveUser(UserDto userDto);

    void promoteToAdmin(Long id);

    void demoteToUser(Long id);

    User findByEmail(String email);

    UserDto findById(Long id);

    List<UserDto> findAllUsers();

    List<Pet> getUserPets(String email);

    List<Agendamento> getUserAgendamentos(String email);

    void updateUser(String email,UserDto userDto,MultipartFile file) throws IllegalStateException, IOException;

    void deleteUser(Long id);

    

}
