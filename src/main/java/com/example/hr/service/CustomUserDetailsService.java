package com.example.hr.service;

import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kiểm tra xem user có tồn tại trong DB không
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
        
        // Trả về đối tượng CustomUserDetails mà Spring Security có thể hiểu
        return new CustomUserDetails(user);
    }
}