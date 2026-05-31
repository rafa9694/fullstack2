package com.jtech.tasklist.ports.output;

import com.jtech.tasklist.application.core.domains.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
