package com.projeto.petshop.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DirectoryCreator {

    @Value("${upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Diretório criado: " + uploadDir);
            } else {
                System.out.println("Falha ao criar diretório: " + uploadDir);
            }
        }
    }
}