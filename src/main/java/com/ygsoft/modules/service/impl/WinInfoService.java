package com.ygsoft.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.ygsoft.common.InitConst;
import com.ygsoft.common.Result;
import com.ygsoft.modules.dao.ISysInfoDao;
import com.ygsoft.modules.dao.IWinInfoDao;
import com.ygsoft.modules.dao.IWinPauseDao;
import com.ygsoft.modules.dao.IWinQueueDao;
import com.ygsoft.modules.entity.*;
import com.ygsoft.modules.service.IWinInfoService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
public class WinInfoService implements IWinInfoService {

    @Autowired
    private IWinInfoDao winInfoDao;

    @Autowired
    private IWinPauseDao winPauseDao;

    @Autowired
    private ISysInfoDao sysInfoDao;

    @Autowired
    private IWinQueueDao winQueueDao;


    public Result init() {
        if (!StringUtils.isNullOrEmpty(InitConst.initFilePath)) {
            String input;
            final SysInfo sysInfo = new SysInfo();
            final List<WinPause> winPauseList = new ArrayList<WinPause>();
            final List<WinQueue> winQueueList = new ArrayList<WinQueue>();
            final List<WinInfo> winInfoList = winInfoDao.findAll();
            //名称、ID
            final Map<String, WinInfo> infoMap = new HashMap<String, WinInfo>();
            for (WinInfo info : winInfoList) {
                infoMap.put(info.getWindowno(), info);
            }
            try {
                input = FileUtils.readFileToString(new File(InitConst.initFilePath), "UTF-8");
                JSONObject initObject = JSONObject.parseObject(input);
                //1、获取最大排队长度
                final int maxQueue = initObject.getInteger("maxQueueLength");
                if (maxQueue < 1) {
                    return Result.error("maxQueueLength设置有误");
                }
                sysInfo.setMaxqueue(maxQueue);
                sysInfo.setLocknum(0);

                //2、获取windowPauseTime
                final JSONObject windowPauseTimeObj = initObject.getJSONObject("windowPauseTime");
                //缓存休息时间，检查时间信息是否有误，计算info的起止时间也需要用到
                Map<String, List<WinPause>> winPauseMap = new HashMap<String, List<WinPause>>();
                if (windowPauseTimeObj != null && !windowPauseTimeObj.isEmpty()) {
                    final Iterator<Map.Entry<String, Object>> iterable = windowPauseTimeObj.entrySet().iterator();

                    while (iterable.hasNext()) {
                        Map.Entry<String, Object> pauseTimeEntity = iterable.next();
                        final String winCode = pauseTimeEntity.getKey();
                        if (!infoMap.containsKey(winCode)) {
                            return Result.error("未知的窗口：" + winCode + ",初始化失败");
                        }
                        if (pauseTimeEntity.getValue() == null) {
                            return Result.error("窗口：" + winCode + "未设置暂停时间,初始化失败");
                        }
                        final String pauseTimeStr = String.valueOf(pauseTimeEntity.getValue());
                        //按小数点进行拆分
                        final String[] startAndEndTimeList = pauseTimeStr.split(",");
                        for (String startAndEndTimeStr : startAndEndTimeList) {
                            final String[] startEndTime = startAndEndTimeStr.split("-");
                            if (startEndTime.length != 2) {
                                return Result.error("窗口：" + winCode + "暂停时间有误,初始化失败");
                            }
                            String startStr = startEndTime[0];
                            String endStr = startEndTime[1];
                            try {
                                BigDecimal startTime = new BigDecimal(startStr.trim());
                                BigDecimal endTime = new BigDecimal(endStr.trim());
                                if (!checkInitPauseTime(startTime, endTime, winPauseMap.get(winCode))) {
                                    return Result.error("窗口：" + winCode + "暂停时间有误,初始化失败");
                                }
                                final WinPause newWinPause = new WinPause();
                                newWinPause.setWindowno(winCode);
                                newWinPause.setStarttime(startTime);
                                newWinPause.setEndtime(endTime);
                                winPauseList.add(newWinPause);
                                if (!winPauseMap.containsKey(winCode)) {
                                    winPauseMap.put(winCode, new ArrayList<WinPause>());
                                }
                                winPauseMap.get(winCode).add(newWinPause);
                            } catch (Exception e) {
                                return Result.error("窗口：" + winCode + "暂停时间有误,初始化失败");
                            }
                        }
                        //按开始时间排序，后面添加已排队信息需要
                        Collections.sort(winPauseMap.get(winCode), (s1, s2) -> s1.getStarttime().compareTo(s2.getStarttime()));
                    }
                }

                //3、获取windowInitQueue
                final JSONObject windowInitQueueObj = initObject.getJSONObject("windowInitQueue");
                if (windowInitQueueObj != null && !windowInitQueueObj.isEmpty()) {
                    final Iterator<Map.Entry<String, Object>> iterable = windowInitQueueObj.entrySet().iterator();
                    final List<String> existWinCode = new ArrayList<String>();
                    while (iterable.hasNext()) {
                        Map.Entry<String, Object> windowInitQueueEntity = iterable.next();
                        final String winCode = windowInitQueueEntity.getKey();
                        if (!infoMap.containsKey(winCode)) {
                            return Result.error("未知的窗口：" + winCode + ",初始化失败");
                        }
                        if (windowInitQueueEntity.getValue() == null) {
                            return Result.error("窗口：" + winCode + "未设置初始化队列,初始化失败");
                        }
                        if (existWinCode.contains(winCode)) {
                            return Result.error("窗口：" + winCode + "初始化队列重复设置,初始化失败");
                        }
                        final String windowInitQueueStr = String.valueOf(windowInitQueueEntity.getValue());
                        final String[] windowInitQueueList = windowInitQueueStr.split(",");
                        //用于计算队列开始、结束时间
                        BigDecimal startTime = new BigDecimal(0);
                        final BigDecimal busiTime = infoMap.get(winCode).getBusitime();
                        final int winId = infoMap.get(winCode).getGid();
                        int i = 1;
                        for (String windowInitQueue : windowInitQueueList) {
                            try {
                                final int tasknum = Integer.parseInt(windowInitQueue.trim());
                                if (tasknum < 0) {
                                    return Result.error("窗口：" + winCode + "初始化队列设置有误,初始化失败");
                                }
                                if (tasknum > 0) {
                                    if (i > maxQueue) {
                                        return Result.error("窗口：" + winCode + "初始化队列超过最大长度,初始化失败");
                                    }
                                    //判断开始时间结束时间实在在暂停时间内，如果时，需要向后移动
                                    BigDecimal endTime = startTime.add(busiTime.multiply(new BigDecimal(tasknum)));
                                    if (winPauseMap.containsKey(winCode)) {
                                        final List<WinPause> tmpWinPause = winPauseMap.get(winCode);
                                        //前面已经按开始时间排序了
                                        for (WinPause winPauseVO : tmpWinPause) {
                                            final BigDecimal pauseStartTime = winPauseVO.getStarttime();
                                            if (endTime.compareTo(pauseStartTime) > 0 && startTime.compareTo(winPauseVO.getEndtime()) <=0) {
                                                startTime = winPauseVO.getEndtime();
                                                endTime = startTime.add(busiTime.multiply(new BigDecimal(tasknum)));
                                            }
                                        }
                                    }

                                    final WinQueue newWinQueue = new WinQueue();
                                    newWinQueue.setStarttime(startTime);
                                    newWinQueue.setEndtime(endTime);
                                    newWinQueue.setWindowno(winCode);
                                    newWinQueue.setTasknum(tasknum);
                                    newWinQueue.setQueueno(i++);
                                    newWinQueue.setEmployeeno(UUID.randomUUID().toString().replaceAll("-", ""));
                                    winQueueList.add(newWinQueue);
                                    startTime = endTime;
                                }
                            } catch (Exception e) {
                                return Result.error("窗口：" + winCode + "初始化队列设置有误,初始化失败");
                            }
                        }
                        existWinCode.add(winCode);
                    }
                }
            } catch (Exception e) {
                return Result.error("读取初始化文件失败");
            }
            sysInfoDao.deleteAll();
            sysInfoDao.save(sysInfo);
            winPauseDao.deleteAll();
            winPauseDao.saveAll(winPauseList);
            winQueueDao.deleteAll();
            winQueueDao.saveAll(winQueueList);
            return Result.ok();
        } else {
            return Result.error("未指定初始化文件路径,初始化失败");
        }
    }

    private boolean checkInitPauseTime(BigDecimal startTime, BigDecimal endTime, List<WinPause> winPauseList) {
        if (startTime.compareTo(endTime) >= 0) {
            return false;
        }
        if (winPauseList == null || winPauseList.isEmpty()) {
            return true;
        }
        for (WinPause winPause : winPauseList) {
            final BigDecimal winStartTime = winPause.getStarttime();
            final BigDecimal winEndTime = winPause.getEndtime();
            if ((winStartTime.compareTo(startTime) >=0 && winStartTime.compareTo(endTime) <0)
                    ||(winEndTime.compareTo(endTime) <=0 && winEndTime.compareTo(startTime) >0)) {
                return false;
            }
        }
        return true;
    }

    public WinQueueDTO getQueueNo(final String empNo,final int items) {
       return doQueryNoDetail(empNo, items);
    }

    private WinQueueDTO doQueryNoDetail(String empNo, int items) {
        //将sys_info的locakNum设置为1，进行独占操作
        int isLock = sysInfoDao.lockNum();
        if (isLock == 0) {
            try {
                Thread.sleep(100);
                return doQueryNoDetail(empNo, items);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            WinQueueDTO result = null;
            //1、根据empNo查询是否在排队中
            WinQueue winQueue = winQueueDao.findByEmployeeno(empNo);
            if (winQueue != null) {
                result = new WinQueueDTO();
                result.setAllWindowExceedMaxWait(false);
                result.setEmployeeNo(winQueue.getEmployeeno());
                result.setQueueNo(winQueue.getQueueno());
                result.setWindowNo(winQueue.getWindowno());
                result.setStartTime(winQueue.getStarttime());
                result.setEndTime(winQueue.getEndtime());
                return result;
            }
            List<SysInfo> sysInfoList = sysInfoDao.findAll();
            if (sysInfoList == null || sysInfoList.isEmpty()) {
                //未初始化
                result = new WinQueueDTO();
                result.setEmployeeNo("未初始化");
                return null;
            }
            final int maxQueue = sysInfoList.get(0).getMaxqueue();
            List<WinQueue> winQueueList = winQueueDao.findMaxQueue();
            final List<String> existQueueWindowNo = new ArrayList<String>();
            if (winQueueList != null && !winQueueList.isEmpty()) {
                for (WinQueue tmpWinQueue : winQueueList) {
                    existQueueWindowNo.add(tmpWinQueue.getWindowno());
                }
            }
            final List<WinInfo> winInfoList = winInfoDao.findAll();
            for (WinInfo winInfo : winInfoList) {
                //如果该窗口没有队列，添加winQueue，设置endtime为0
                if (!existQueueWindowNo.contains(winInfo.getWindowno())) {
                    WinQueue tmpWinQueue = new WinQueue();
                    tmpWinQueue.setWindowno(winInfo.getWindowno());
                    tmpWinQueue.setQueueno(0);
                    tmpWinQueue.setEndtime(new BigDecimal(0));
                    winQueueList.add(tmpWinQueue);
                }
            }
            //去掉queueNo >= maxQueue的
            for (int i = winQueueList.size() - 1; i>=0; i-- ) {
                final WinQueue tmpWinQueue = winQueueList.get(i);
                if (tmpWinQueue.getQueueno() >= maxQueue) {
                    winQueueList.remove(i);
                }
            }
            //如果没有符合条件的，直接返回
            if (winQueueList.isEmpty()) {
                result = new WinQueueDTO();
                result.setAllWindowExceedMaxWait(true);
                return result;
            }
            //对winQueueList中的数据进行分析,获取时间并对endtime进行排序，取第一条
            return result = getBestWinQueue(empNo, items, winQueueList, winInfoList);
        } finally {
            sysInfoDao.unlockNum();
        }

    }

    private WinQueueDTO getBestWinQueue(String empNo, int items, final List<WinQueue> winQueueList, final List<WinInfo> winInfoList) {
        final List<WinPause> winPauseList = winPauseDao.findAll();
        final Map<String, List<WinPause>> windownoPauseListMap = new HashMap<String, List<WinPause>>();
        for (WinPause winPause : winPauseList) {
            if (!windownoPauseListMap.containsKey(winPause.getWindowno())) {
                windownoPauseListMap.put(winPause.getWindowno(), new ArrayList<WinPause>());
            }
            windownoPauseListMap.get(winPause.getWindowno()).add(winPause);
        }
        final Map<String, BigDecimal> windowNoBusiTimeMap = new HashMap<String, BigDecimal>();
        for (WinInfo winInfo : winInfoList) {
            windowNoBusiTimeMap.put(winInfo.getWindowno(), winInfo.getBusitime());
        }
        WinQueue newWinQueue = null;
        for (WinQueue winQueue : winQueueList) {
            final String windowNo = winQueue.getWindowno();
            final BigDecimal busiTime = windowNoBusiTimeMap.get(windowNo);
            final int queueNo = winQueue.getQueueno() + 1;
            BigDecimal startTime = winQueue.getEndtime();
            BigDecimal endTime = startTime.add(busiTime.multiply(new BigDecimal(items)));
            final List<WinPause> tmpWinPauseList = windownoPauseListMap.get(windowNo);
            if (tmpWinPauseList != null && !tmpWinPauseList.isEmpty()) {
                for (WinPause winPause : tmpWinPauseList) {
                    if (endTime.compareTo(winPause.getStarttime()) > 0 && startTime.compareTo(winPause.getEndtime()) <=0) {
                        startTime = winPause.getEndtime();
                        endTime = startTime.add(busiTime.multiply(new BigDecimal(items)));
                    }
                }
                if (newWinQueue == null || newWinQueue.getEndtime().compareTo(endTime) > 0) {
                    newWinQueue = new WinQueue();
                    newWinQueue.setWindowno(windowNo);
                    newWinQueue.setQueueno(queueNo);
                    newWinQueue.setTasknum(items);
                    newWinQueue.setStarttime(startTime);
                    newWinQueue.setEndtime(endTime);
                    newWinQueue.setEmployeeno(empNo);
                }
            }
        }
        winQueueDao.save(newWinQueue);
        final WinQueueDTO result = new WinQueueDTO();
        result.setAllWindowExceedMaxWait(false);
        result.setWindowNo(newWinQueue.getWindowno());
        result.setStartTime(newWinQueue.getStarttime());
        result.setEndTime(newWinQueue.getEndtime());
        result.setQueueNo(newWinQueue.getQueueno());
        result.setEmployeeNo(newWinQueue.getEmployeeno());
        return result;
    }

}
