package com.jtech.tasklist.application.core.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.application.core.exceptions.ResourceNotFoundException;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.ports.input.GetTaskPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetTaskUseCase implements GetTaskPort {

    private final TaskRepositoryPort taskRepositoryPort;

    public GetTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Task execute(Long id, Long userId) {
        Task task = taskRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa", id));

        if (!task.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        return task;
    }
}
