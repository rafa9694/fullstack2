package com.jtech.tasklist.application.core.usecases.tasklist;

import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.application.core.exceptions.ResourceNotFoundException;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.ports.input.UpdateTaskListPort;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UpdateTaskListUseCase implements UpdateTaskListPort {

    private final TaskListRepositoryPort taskListRepositoryPort;

    public UpdateTaskListUseCase(TaskListRepositoryPort taskListRepositoryPort) {
        this.taskListRepositoryPort = taskListRepositoryPort;
    }

    @Override
    public TaskList execute(Long id, String name, Long userId) {
        TaskList existing = taskListRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lista", id));

        if (!existing.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        TaskList updated = TaskList.builder()
                .id(existing.getId())
                .name(name)
                .userId(userId)
                .build();

        return taskListRepositoryPort.save(updated);
    }
}
