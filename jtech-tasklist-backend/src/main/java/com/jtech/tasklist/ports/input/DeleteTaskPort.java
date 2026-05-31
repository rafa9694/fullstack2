package com.jtech.tasklist.ports.input;

public interface DeleteTaskPort {
    void execute(Long id, Long userId);
}
