package com.ygsoft.modules.controller;

import com.ygsoft.common.Result;
import com.ygsoft.modules.entity.WinQueueDTO;

public interface IWinInfoController {

    Result init();

    String getQueueNo(String empNo, int items);

    WinQueueDTO doQueryNoDetail(String empNo, int items);

}
