package com.ygsoft.modules.controller.impl;

import com.mysql.jdbc.StringUtils;
import com.ygsoft.common.InitConst;
import com.ygsoft.common.Result;
import com.ygsoft.modules.controller.IWinInfoController;
import com.ygsoft.modules.entity.TaskResultDTO;
import com.ygsoft.modules.entity.TastDTO;
import com.ygsoft.modules.entity.WinQueueDTO;
import com.ygsoft.modules.service.IWinInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.util.UUID;

import static java.lang.Thread.sleep;

@RestController
public class WinInfoController implements IWinInfoController {


    private final TaskResultDTO taskDto = new TaskResultDTO();

    @Autowired
    private IWinInfoService service;

    @RequestMapping("/init")
    @Override
    @Transactional
    public Result init(){
        return service.init();
    }

    @PostConstruct
    public void initMethod(){
        taskThread.start();
    }

    private Thread taskThread = new WinQueueThread(taskDto);

    @RequestMapping("/getQueueNo")
    @Override
    public WinQueueDTO getQueueNo(String empNo, int items){
        final TastDTO dto = new TastDTO();
        dto.setItems(items);
        dto.setEmpNo(empNo);
        final String gid = UUID.randomUUID().toString();
        dto.setGid(gid);
        taskDto.addTask(dto);
        while (true) {
            final WinQueueDTO result = taskDto.getResult(gid);
            if (result == null) {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return result;
            }
        }
    }

    @Transactional
    public WinQueueDTO doQueryNoDetail(String empNo, int items) {
        return this.service.doQueryNoDetail(empNo, items);
    }

    class WinQueueThread extends Thread {

        TaskResultDTO taskDto = null;
        public WinQueueThread(TaskResultDTO taskDto) {
            this.taskDto = taskDto;
        }

        public void run() {
            while (true) {
                synchronized(taskDto) {
                    if (taskDto.isEmpty()) {
                        try {
                            System.out.println("taskDto为空，等待2秒钟");
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        final TastDTO task = taskDto.getTask();
                        final WinQueueDTO TaskResult = doQueryNoDetail(task.getEmpNo(), task.getItems());
                        taskDto.addResult(task.getGid(), TaskResult);
                    }
                }
            }
        }
    }
}
