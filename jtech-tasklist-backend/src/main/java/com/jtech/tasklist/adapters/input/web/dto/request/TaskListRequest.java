package com.jtech.tasklist.adapters.input.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TaskListRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name
) {}
