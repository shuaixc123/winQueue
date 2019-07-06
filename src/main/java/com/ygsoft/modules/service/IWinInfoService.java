package com.ygsoft.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.ygsoft.common.Result;
import com.ygsoft.modules.entity.WinInfo;
import com.ygsoft.modules.entity.WinQueueDTO;

public interface IWinInfoService {

    Result init();

    WinQueueDTO doQueryNoDetail(String empNo, int items);
}
