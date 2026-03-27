package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.dto.request.UserRequest;
import com.qiraht.ticket_order.entity.User;
import com.qiraht.ticket_order.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(UserRequest request) {
        // TODO: add email checks

        // TODO: add password hashing

        User user = User.builder()
                .email(request.email())
                .firstName(request.first_name())
                .lastName(request.last_name())
                .password(request.password())
                .build();

        userRepository.save(user);

        return user.getId().toString();
    }
}
