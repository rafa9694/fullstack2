package com.jtech.tasklist.adapters.output.persistence.mappers;

import com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import com.jtech.tasklist.adapters.output.persistence.entities.TaskListEntity;
import com.jtech.tasklist.adapters.output.persistence.entities.UserEntity;
import com.jtech.tasklist.application.core.domains.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {

    public Task toDomain(TaskEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .completed(entity.isCompleted())
                .createdAt(entity.getCreatedAt())
                .taskListId(entity.getTaskList().getId())
                .userId(entity.getUser().getId())
                .build();
    }

    public TaskEntity toEntity(Task domain) {
        return TaskEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .completed(domain.isCompleted())
                .createdAt(domain.getCreatedAt())
                .taskList(TaskListEntity.builder().id(domain.getTaskListId()).build())
                .user(UserEntity.builder().id(domain.getUserId()).build())
                .build();
    }
}
