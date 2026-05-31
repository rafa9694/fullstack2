package com.jtech.tasklist.application.core.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.application.core.exceptions.ResourceNotFoundException;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.ports.input.UpdateTaskPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UpdateTaskUseCase implements UpdateTaskPort {

    private final TaskRepositoryPort taskRepositoryPort;

    public UpdateTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Task execute(Long id, Task task, Long userId) {
        Task existing = taskRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa", id));

        if (!existing.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        Task updated = Task.builder()
                .id(existing.getId())
                .title(task.getTitle() != null ? task.getTitle() : existing.getTitle())
                .description(task.getDescription() != null ? task.getDescription() : existing.getDescription())
                .completed(existing.isCompleted())
                .createdAt(existing.getCreatedAt())
                .taskListId(existing.getTaskListId())
                .userId(existing.getUserId())
                .build();

        return taskRepositoryPort.save(updated);
    }
}
