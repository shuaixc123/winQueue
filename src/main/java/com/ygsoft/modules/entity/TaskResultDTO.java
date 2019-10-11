package com.ygsoft.modules.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskResultDTO {

    private List<TastDTO> taskList = new ArrayList<TastDTO>();

    private Map<String, WinQueueDTO> taskResultMap = new HashMap<String, WinQueueDTO>();

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    public WinQueueDTO getResult(String gid) {
        return taskResultMap.remove(gid);
    }

    public void addResult(String gid, WinQueueDTO result) {
        taskResultMap.put(gid, result);
    }

    public void addTask(TastDTO task) {
        taskList.add(task);
    }

    public TastDTO getTask() {
        return taskList.remove(0);
    }

}
