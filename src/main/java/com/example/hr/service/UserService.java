package com.example.hr.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser(User user) {
        // Luôn mã hóa mật khẩu trước khi lưu vào DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveUser(User user, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // Trỏ vào thư mục public/test1
            Path staticPath = Paths.get("public/test1");

            if (!Files.exists(staticPath)) {
                Files.createDirectories(staticPath);
            }

            Path filePath = staticPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfileImage(fileName);
        }
        // Mã hóa mật khẩu và lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}