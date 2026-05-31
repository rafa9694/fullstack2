package com.jtech.tasklist.ports.input;

import com.jtech.tasklist.application.core.domains.TaskList;
import java.util.List;

public interface CreateTaskListPort {
    TaskList execute(TaskList taskList, Long userId);
}
