package com.ygsoft.modules.controller;

import com.ygsoft.common.Result;
import com.ygsoft.modules.entity.WinQueueDTO;

public interface IWinInfoController {

    Result init();

    WinQueueDTO getQueueNo(String empNo, int items);

    WinQueueDTO doQueryNoDetail(String empNo, int items);

}
