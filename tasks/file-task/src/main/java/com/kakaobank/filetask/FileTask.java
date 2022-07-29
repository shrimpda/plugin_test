package com.kakaobank.filetask;

import com.kakaobank.kosmos.task.Task;
import com.kakaobank.kosmos.task.TaskComponent;
import com.kakaobank.kosmos.task.TaskService;

import java.net.HttpURLConnection;

@TaskComponent
public class FileTask implements Task {

    private final HttpURLConnection httpURLConnection = null;
    private final int a = 2;
    private final int b = 1;
    private final TaskService taskService;

    public FileTask(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(String value) {
        System.out.println(value + b);
    }
}
