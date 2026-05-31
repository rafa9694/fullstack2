package com.jtech.tasklist.infrastructure.security;

import com.jtech.tasklist.adapters.output.persistence.repositories.UserJpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserContext {

    private final UserJpaRepository userJpaRepository;

    public CurrentUserContext(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"))
                .getId();
    }
}
