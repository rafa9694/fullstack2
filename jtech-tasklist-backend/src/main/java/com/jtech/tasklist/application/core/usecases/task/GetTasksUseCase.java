package com.jtech.tasklist.application.core.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.ports.input.GetTasksPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTasksUseCase implements GetTasksPort {

    private final TaskRepositoryPort taskRepositoryPort;

    public GetTasksUseCase(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public List<Task> execute(Long taskListId, Long userId) {
        return taskRepositoryPort.findAllByTaskListIdAndUserId(taskListId, userId);
    }
}
