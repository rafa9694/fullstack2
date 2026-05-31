package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.TaskList;

public interface UpdateTaskListPort {
    TaskList execute(Long id, String name, Long userId);
}
