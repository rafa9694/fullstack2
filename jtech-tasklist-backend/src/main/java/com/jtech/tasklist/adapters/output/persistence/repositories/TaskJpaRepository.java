package com.jtech.tasklist.adapters.output.persistence.repositories;

import com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByTaskListIdAndUserId(Long taskListId, Long userId);
    boolean existsByTitleAndTaskListIdAndUserId(String title, Long taskListId, Long userId);
}
