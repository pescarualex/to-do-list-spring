package com.example.todolistspring.service;

import com.example.todolistspring.domain.Task;
import com.example.todolistspring.exception.ResourceNotFoundException;
import com.example.todolistspring.persistance.TaskRepository;
import com.example.todolistspring.transfer.CreateTaskRequest;
import com.example.todolistspring.transfer.UpdateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(CreateTaskRequest request) {
        LOGGER.info("Creating task: " + request);

        Task task = new Task();
        task.setDeadline(request.getDeadline());
        task.setDescription(request.getDescription());
        task.setDone(request.isDone());

        return taskRepository.save(task);
    }

    public Task getTask(long id) {
        LOGGER.info("Get task with id: " + id);

        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task " + id + " does not exist"));
    }


    public List<Task> getTasks() {
        LOGGER.info("Getting all the tasks.");

        return taskRepository.findAll();
    }

    public Task updateTask(long id, UpdateTask request) {
        LOGGER.info("Update task " + id + " with " + request);

        Task updateTask = getTask(id);

        BeanUtils.copyProperties(request, updateTask);

        return taskRepository.save(updateTask);
    }

    @Transactional
    public void deleteTask(long id) {
        LOGGER.info("Deleting task " + id);

        taskRepository.deleteById(id);
    }


}
