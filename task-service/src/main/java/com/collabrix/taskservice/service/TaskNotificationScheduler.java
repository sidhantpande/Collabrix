package com.collabrix.taskservice.service;

import com.collabrix.taskservice.events.TaskEventPublisher;
import com.collabrix.taskservice.model.entities.Task;
import com.collabrix.taskservice.model.enums.TaskStatus;
import com.collabrix.taskservice.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskNotificationScheduler {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    public TaskNotificationScheduler(TaskRepository taskRepository, TaskEventPublisher taskEventPublisher) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }

    @Scheduled(cron = "${app.task.due-soon-cron:0 0 8 * * *}")
    public void publishDueSoonEvents() {
        LocalDate targetDate = LocalDate.now().plusDays(1);
        List<Task> dueSoonTasks = taskRepository.findByDueDateAndStatusNot(targetDate, TaskStatus.COMPLETED);
        for (Task task : dueSoonTasks) {
            taskEventPublisher.publish("TASK_DUE_SOON", task, task.getCreatedByAuthId(), task.getList().getBoard().getName());
        }
    }
}
