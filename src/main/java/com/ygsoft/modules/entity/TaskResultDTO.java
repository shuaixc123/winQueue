package com.ygsoft.modules.entity;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class TaskResultDTO {

    private Vector<TastDTO> taskList = new Vector <TastDTO>();

    private Map<String, WinQueueDTO> taskResultMap = new ConcurrentHashMap<String, WinQueueDTO>();

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
