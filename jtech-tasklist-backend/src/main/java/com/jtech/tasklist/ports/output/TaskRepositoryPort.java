package com.jtech.tasklist.ports.output;

import com.jtech.tasklist.application.core.domains.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAllByTaskListIdAndUserId(Long taskListId, Long userId);
    void deleteById(Long id);
    boolean existsByTitleAndTaskListIdAndUserId(String title, Long taskListId, Long userId);
}
