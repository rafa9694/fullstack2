package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.Task;
import java.util.List;

public interface GetTasksPort {
    List<Task> execute(Long taskListId, Long userId);
}
