package com.jtech.tasklist.adapters.input.web.dto.request;

public record UpdateTaskRequest(
        String title,
        String description
) {}
