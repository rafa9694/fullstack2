package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.Task;

public interface CreateTaskPort {
    Task execute(Task task, Long userId);
}
