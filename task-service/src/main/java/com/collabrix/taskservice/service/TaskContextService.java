package com.collabrix.taskservice.service;

import com.collabrix.taskservice.exception.TaskServiceException;
import com.collabrix.taskservice.model.dto.response.TaskCommentContextResponseDTO;
import com.collabrix.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskContextService {

    private final TaskRepository taskRepository;

    public TaskContextService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskCommentContextResponseDTO getTaskCommentContext(UUID taskId) {
        return taskRepository.findTaskContextById(taskId)
                .map(TaskCommentContextResponseDTO::fromEntity)
                .orElseThrow(TaskServiceException::taskNotFound);
    }
}
