package com.jtech.tasklist.adapters.output.persistence.mappers;

import com.jtech.tasklist.adapters.output.persistence.entities.TaskListEntity;
import com.jtech.tasklist.adapters.output.persistence.entities.UserEntity;
import com.jtech.tasklist.application.core.domains.TaskList;
import org.springframework.stereotype.Component;

@Component
public class TaskListPersistenceMapper {

    public TaskList toDomain(TaskListEntity entity) {
        return TaskList.builder()
                .id(entity.getId())
                .name(entity.getName())
                .userId(entity.getUser().getId())
                .build();
    }

    public TaskListEntity toEntity(TaskList domain) {
        return TaskListEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .user(UserEntity.builder().id(domain.getUserId()).build())
                .build();
    }
}
