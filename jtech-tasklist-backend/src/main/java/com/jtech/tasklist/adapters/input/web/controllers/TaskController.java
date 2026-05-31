package com.jtech.tasklist.adapters.input.web.controllers;

import com.jtech.tasklist.adapters.input.web.dto.request.TaskRequest;
import com.jtech.tasklist.adapters.input.web.dto.request.UpdateTaskRequest;
import com.jtech.tasklist.adapters.input.web.dto.response.TaskResponse;
import com.jtech.tasklist.application.core.domains.Task;
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
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "CRUD de tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final CreateTaskPort createTaskPort;
    private final GetTasksPort getTasksPort;
    private final GetTaskPort getTaskPort;
    private final UpdateTaskPort updateTaskPort;
    private final DeleteTaskPort deleteTaskPort;
    private final CompleteTaskPort completeTaskPort;
    private final CurrentUserContext currentUserContext;

    public TaskController(CreateTaskPort createTaskPort,
                          GetTasksPort getTasksPort,
                          GetTaskPort getTaskPort,
                          UpdateTaskPort updateTaskPort,
                          DeleteTaskPort deleteTaskPort,
                          CompleteTaskPort completeTaskPort,
                          CurrentUserContext currentUserContext) {
        this.createTaskPort = createTaskPort;
        this.getTasksPort = getTasksPort;
        this.getTaskPort = getTaskPort;
        this.updateTaskPort = updateTaskPort;
        this.deleteTaskPort = deleteTaskPort;
        this.completeTaskPort = completeTaskPort;
        this.currentUserContext = currentUserContext;
    }

    @GetMapping
    @Operation(summary = "Listar tarefas de uma lista")
    public List<TaskResponse> getAll(@RequestParam Long taskListId) {
        Long userId = currentUserContext.getCurrentUserId();
        return getTasksPort.execute(taskListId, userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public TaskResponse getById(@PathVariable Long id) {
        Long userId = currentUserContext.getCurrentUserId();
        return toResponse(getTaskPort.execute(id, userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova tarefa")
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        Long userId = currentUserContext.getCurrentUserId();
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .taskListId(request.taskListId())
                .build();
        return toResponse(createTaskPort.execute(task, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa")
    public TaskResponse update(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        Long userId = currentUserContext.getCurrentUserId();
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .build();
        return toResponse(updateTaskPort.execute(id, task, userId));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Marcar tarefa como concluída")
    public TaskResponse complete(@PathVariable Long id) {
        Long userId = currentUserContext.getCurrentUserId();
        return toResponse(completeTaskPort.execute(id, userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir tarefa")
    public void delete(@PathVariable Long id) {
        Long userId = currentUserContext.getCurrentUserId();
        deleteTaskPort.execute(id, userId);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.isCompleted(), task.getCreatedAt(),
                task.getTaskListId(), task.getUserId()
        );
    }
}
