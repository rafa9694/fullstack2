package com.jtech.tasklist.adapters.input.web.controllers;

import com.jtech.tasklist.adapters.input.web.dto.request.TaskListRequest;
import com.jtech.tasklist.adapters.input.web.dto.response.TaskListResponse;
import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.infrastructure.security.CurrentUserContext;
import com.jtech.tasklist.ports.input.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-lists")
@Tag(name = "Listas de Tarefas", description = "CRUD de listas")
@SecurityRequirement(name = "bearerAuth")
public class TaskListController {

    private final CreateTaskListPort createTaskListPort;
    private final GetTaskListsPort getTaskListsPort;
    private final UpdateTaskListPort updateTaskListPort;
    private final DeleteTaskListPort deleteTaskListPort;
    private final CurrentUserContext currentUserContext;

    public TaskListController(CreateTaskListPort createTaskListPort,
                              GetTaskListsPort getTaskListsPort,
                              UpdateTaskListPort updateTaskListPort,
                              DeleteTaskListPort deleteTaskListPort,
                              CurrentUserContext currentUserContext) {
        this.createTaskListPort = createTaskListPort;
        this.getTaskListsPort = getTaskListsPort;
        this.updateTaskListPort = updateTaskListPort;
        this.deleteTaskListPort = deleteTaskListPort;
        this.currentUserContext = currentUserContext;
    }

    @GetMapping
    @Operation(summary = "Listar todas as listas do usuário")
    public List<TaskListResponse> getAll() {
        Long userId = currentUserContext.getCurrentUserId();
        return getTaskListsPort.execute(userId).stream()
                .map(tl -> new TaskListResponse(tl.getId(), tl.getName(), tl.getUserId()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova lista")
    public TaskListResponse create(@Valid @RequestBody TaskListRequest request) {
        Long userId = currentUserContext.getCurrentUserId();
        TaskList created = createTaskListPort.execute(
                TaskList.builder().name(request.name()).build(), userId);
        return new TaskListResponse(created.getId(), created.getName(), created.getUserId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lista")
    public TaskListResponse update(@PathVariable Long id, @Valid @RequestBody TaskListRequest request) {
        Long userId = currentUserContext.getCurrentUserId();
        TaskList updated = updateTaskListPort.execute(id, request.name(), userId);
        return new TaskListResponse(updated.getId(), updated.getName(), updated.getUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir lista")
    public void delete(@PathVariable Long id) {
        Long userId = currentUserContext.getCurrentUserId();
        deleteTaskListPort.execute(id, userId);
    }
}
