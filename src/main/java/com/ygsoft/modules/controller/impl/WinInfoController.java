package com.ygsoft.modules.controller.impl;

import com.mysql.jdbc.StringUtils;
import com.ygsoft.common.InitConst;
import com.ygsoft.common.Result;
import com.ygsoft.modules.controller.IWinInfoController;
import com.ygsoft.modules.entity.WinQueueDTO;
import com.ygsoft.modules.service.IWinInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class WinInfoController implements IWinInfoController {

    @Autowired
    private IWinInfoService service;

    @RequestMapping("/init")
    @Override
    @Transactional
    public Result init(){
        return service.init();
    }

    @RequestMapping("/getQueueNo")
    @Override
    @Transactional
    public WinQueueDTO getQueueNo(String empNo, int items){
        return service.getQueueNo(empNo, items);
    }
}
