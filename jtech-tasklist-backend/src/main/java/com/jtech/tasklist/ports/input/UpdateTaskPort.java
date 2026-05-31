package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.Task;

public interface UpdateTaskPort {
    Task execute(Long id, Task task, Long userId);
}
