package com.jtech.tasklist.adapters.output.persistence.repositories;

import com.jtech.tasklist.adapters.output.persistence.entities.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListJpaRepository extends JpaRepository<TaskListEntity, Long> {
    List<TaskListEntity> findAllByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}
