package com.collabrix.taskservice.model.dto.request;

import com.collabrix.taskservice.model.enums.TaskPriority;
import com.collabrix.taskservice.model.enums.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateTaskRequest {

    @Size(min = 1, max = 255, message = "Task title must be between 1 and 255 characters")
    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private UUID completedByAuthId;

    private UUID assigneeAuthId;

    private LocalDate dueDate;
}
