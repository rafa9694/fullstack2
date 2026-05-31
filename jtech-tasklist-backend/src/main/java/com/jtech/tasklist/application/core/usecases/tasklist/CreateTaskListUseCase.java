package com.jtech.tasklist.application.core.usecases.tasklist;

import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.ports.input.CreateTaskListPort;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskListUseCase implements CreateTaskListPort {

    private final TaskListRepositoryPort taskListRepositoryPort;

    public CreateTaskListUseCase(TaskListRepositoryPort taskListRepositoryPort) {
        this.taskListRepositoryPort = taskListRepositoryPort;
    }

    @Override
    public TaskList execute(TaskList taskList, Long userId) {
        if (taskListRepositoryPort.existsByNameAndUserId(taskList.getName(), userId)) {
            throw new IllegalArgumentException("Já existe uma lista com este nome");
        }

        TaskList toSave = TaskList.builder()
                .name(taskList.getName())
                .userId(userId)
                .build();

        return taskListRepositoryPort.save(toSave);
    }
}
