package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.Task;

public interface CompleteTaskPort {
    Task execute(Long id, Long userId);
}
