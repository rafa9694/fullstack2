package com.jtech.tasklist.application.core.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.application.core.exceptions.ResourceNotFoundException;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.ports.input.DeleteTaskPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskUseCase implements DeleteTaskPort {

    private final TaskRepositoryPort taskRepositoryPort;

    public DeleteTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public void execute(Long id, Long userId) {
        Task task = taskRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa", id));

        if (!task.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        taskRepositoryPort.deleteById(id);
    }
}
