package com.jtech.tasklist.ports.input;

public interface DeleteTaskListPort {
    void execute(Long id, Long userId);
}
