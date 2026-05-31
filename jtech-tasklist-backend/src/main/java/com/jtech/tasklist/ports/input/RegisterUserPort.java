package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.User;

public interface RegisterUserPort {
    User execute(User user);
}
