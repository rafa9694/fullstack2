package com.jtech.tasklist.adapters.output.persistence;

import com.jtech.tasklist.adapters.output.persistence.mappers.TaskListPersistenceMapper;
import com.jtech.tasklist.adapters.output.persistence.repositories.TaskListJpaRepository;
import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListRepositoryAdapter implements TaskListRepositoryPort {

    private final TaskListJpaRepository taskListJpaRepository;
    private final TaskListPersistenceMapper mapper;

    public TaskListRepositoryAdapter(TaskListJpaRepository taskListJpaRepository, TaskListPersistenceMapper mapper) {
        this.taskListJpaRepository = taskListJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TaskList save(TaskList taskList) {
        return mapper.toDomain(taskListJpaRepository.save(mapper.toEntity(taskList)));
    }

    @Override
    public Optional<TaskList> findById(Long id) {
        return taskListJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TaskList> findAllByUserId(Long userId) {
        return taskListJpaRepository.findAllByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        taskListJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndUserId(String name, Long userId) {
        return taskListJpaRepository.existsByNameAndUserId(name, userId);
    }
}
