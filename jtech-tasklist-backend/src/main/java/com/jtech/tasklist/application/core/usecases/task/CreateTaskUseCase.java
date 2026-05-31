package com.jtech.tasklist.application.core.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.application.core.exceptions.ResourceNotFoundException;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.ports.input.CreateTaskPort;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateTaskUseCase implements CreateTaskPort {

    private final TaskRepositoryPort taskRepositoryPort;
    private final TaskListRepositoryPort taskListRepositoryPort;

    public CreateTaskUseCase(TaskRepositoryPort taskRepositoryPort,
                             TaskListRepositoryPort taskListRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.taskListRepositoryPort = taskListRepositoryPort;
    }

    @Override
    public Task execute(Task task, Long userId) {
        TaskList taskList = taskListRepositoryPort.findById(task.getTaskListId())
                .orElseThrow(() -> new ResourceNotFoundException("Lista", task.getTaskListId()));

        if (!taskList.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        if (taskRepositoryPort.existsByTitleAndTaskListIdAndUserId(task.getTitle(), task.getTaskListId(), userId)) {
            throw new IllegalArgumentException("Já existe uma tarefa com este título nesta lista");
        }

        Task toSave = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .taskListId(task.getTaskListId())
                .userId(userId)
                .build();

        return taskRepositoryPort.save(toSave);
    }
}
