package com.jtech.tasklist.application.usecases.tasklist;

import com.jtech.tasklist.application.core.domains.TaskList;
import com.jtech.tasklist.application.core.usecases.tasklist.CreateTaskListUseCase;
import com.jtech.tasklist.ports.output.TaskListRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTaskListUseCase")
class CreateTaskListUseCaseTest {

    @Mock private TaskListRepositoryPort taskListRepositoryPort;
    @InjectMocks private CreateTaskListUseCase useCase;

    @Test
    @DisplayName("deve criar lista com sucesso")
    void shouldCreateTaskListSuccessfully() {
        TaskList input = TaskList.builder().name("Trabalho").build();
        TaskList saved = TaskList.builder().id(1L).name("Trabalho").userId(10L).build();

        when(taskListRepositoryPort.existsByNameAndUserId("Trabalho", 10L)).thenReturn(false);
        when(taskListRepositoryPort.save(any())).thenReturn(saved);

        TaskList result = useCase.execute(input, 10L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Trabalho");
        assertThat(result.getUserId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("deve lançar exceção quando lista com mesmo nome já existe")
    void shouldThrowWhenDuplicateName() {
        TaskList input = TaskList.builder().name("Trabalho").build();
        when(taskListRepositoryPort.existsByNameAndUserId("Trabalho", 10L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(input, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");

        verify(taskListRepositoryPort, never()).save(any());
    }
}
