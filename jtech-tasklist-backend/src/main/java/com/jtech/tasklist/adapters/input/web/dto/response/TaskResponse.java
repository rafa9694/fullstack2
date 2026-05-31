package com.jtech.tasklist.adapters.input.web.dto.response;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt,
        Long taskListId,
        Long userId
) {}
