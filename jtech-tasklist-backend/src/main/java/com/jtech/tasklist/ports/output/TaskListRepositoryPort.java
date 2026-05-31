package com.jtech.tasklist.ports.output;

import com.jtech.tasklist.application.core.domains.TaskList;
import java.util.List;
import java.util.Optional;

public interface TaskListRepositoryPort {
    TaskList save(TaskList taskList);
    Optional<TaskList> findById(Long id);
    List<TaskList> findAllByUserId(Long userId);
    void deleteById(Long id);
    boolean existsByNameAndUserId(String name, Long userId);
}
