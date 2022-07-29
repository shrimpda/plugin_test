package com.kakaobank.filetask;

import com.kakaobank.kosmos.task.AgentService;
import com.kakaobank.kosmos.task.Task;

import java.net.HttpURLConnection;

public class FileTaskOther implements Task {

    private final HttpURLConnection httpURLConnection = null;
    private final int a = 2;
    private final int b = 1;
    private final AgentService agentService;

    public FileTaskOther(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public void execute(String value) {
        System.out.println(agentService);
    }
}
