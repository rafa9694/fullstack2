package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.Task;

public interface GetTaskPort {
    Task execute(Long id, Long userId);
}
