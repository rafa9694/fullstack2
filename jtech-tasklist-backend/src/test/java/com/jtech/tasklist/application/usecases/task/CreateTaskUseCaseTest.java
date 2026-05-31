package com.jtech.tasklist.application.usecases.task;

import com.jtech.tasklist.application.core.domains.Task;
import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.application.core.exceptions.UnauthorizedAccessException;
import com.jtech.tasklist.application.core.usecases.task.CreateTaskUseCase;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import com.jtech.tasklist.ports.output.TaskRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTaskUseCase")
class CreateTaskUseCaseTest {

    @Mock private TaskRepositoryPort taskRepositoryPort;
    @Mock private TaskListRepositoryPort taskListRepositoryPort;
    @InjectMocks private CreateTaskUseCase useCase;

    @Test
    @DisplayName("deve criar tarefa com sucesso")
    void shouldCreateTaskSuccessfully() {
        TaskList taskList = TaskList.builder().id(1L).userId(10L).build();
        Task input = Task.builder().title("Nova tarefa").taskListId(1L).build();
        Task saved = Task.builder().id(1L).title("Nova tarefa").taskListId(1L).userId(10L).build();

        when(taskListRepositoryPort.findById(1L)).thenReturn(Optional.of(taskList));
        when(taskRepositoryPort.existsByTitleAndTaskListIdAndUserId("Nova tarefa", 1L, 10L)).thenReturn(false);
        when(taskRepositoryPort.save(any())).thenReturn(saved);

        Task result = useCase.execute(input, 10L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Nova tarefa");
    }

    @Test
    @DisplayName("deve lançar exceção quando usuário não é dono da lista")
    void shouldThrowWhenUserNotOwnerOfList() {
        TaskList taskList = TaskList.builder().id(1L).userId(99L).build();
        Task input = Task.builder().title("Tarefa").taskListId(1L).build();

        when(taskListRepositoryPort.findById(1L)).thenReturn(Optional.of(taskList));

        assertThatThrownBy(() -> useCase.execute(input, 10L))
                .isInstanceOf(UnauthorizedAccessException.class);

        verify(taskRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando tarefa duplicada na lista")
    void shouldThrowWhenDuplicateTask() {
        TaskList taskList = TaskList.builder().id(1L).userId(10L).build();
        Task input = Task.builder().title("Tarefa duplicada").taskListId(1L).build();

        when(taskListRepositoryPort.findById(1L)).thenReturn(Optional.of(taskList));
        when(taskRepositoryPort.existsByTitleAndTaskListIdAndUserId("Tarefa duplicada", 1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(input, 10L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
