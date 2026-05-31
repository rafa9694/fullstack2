package com.jtech.tasklist.application.core.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private Long taskListId;
    private Long userId;

    public Task complete() {
        return Task.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .completed(true)
                .createdAt(this.createdAt)
                .taskListId(this.taskListId)
                .userId(this.userId)
                .build();
    }
}
