package com.jtech.tasklist.application.core.usecases.tasklist;

import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.ports.input.GetTaskListsPort;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTaskListsUseCase implements GetTaskListsPort {

    private final TaskListRepositoryPort taskListRepositoryPort;

    public GetTaskListsUseCase(TaskListRepositoryPort taskListRepositoryPort) {
        this.taskListRepositoryPort = taskListRepositoryPort;
    }

    @Override
    public List<TaskList> execute(Long userId) {
        return taskListRepositoryPort.findAllByUserId(userId);
    }
}
