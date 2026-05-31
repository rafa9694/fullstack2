package com.jtech.tasklist.adapters.output.persistence;

import com.jtech.tasklist.adapters.output.persistence.mappers.TaskPersistenceMapper;
import com.jtech.tasklist.adapters.output.persistence.repositories.TaskJpaRepository;
import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskPersistenceMapper mapper;

    public TaskRepositoryAdapter(TaskJpaRepository taskJpaRepository, TaskPersistenceMapper mapper) {
        this.taskJpaRepository = taskJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Task save(Task task) {
        return mapper.toDomain(taskJpaRepository.save(mapper.toEntity(task)));
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Task> findAllByTaskListIdAndUserId(Long taskListId, Long userId) {
        return taskJpaRepository.findAllByTaskListIdAndUserId(taskListId, userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByTitleAndTaskListIdAndUserId(String title, Long taskListId, Long userId) {
        return taskJpaRepository.existsByTitleAndTaskListIdAndUserId(title, taskListId, userId);
    }
}
