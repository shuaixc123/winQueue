package com.ygsoft.modules.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

public class WinQueueDTO implements Serializable {

    private boolean allWindowExceedMaxWait;

    private String employeeNo;

    private String windowNo;

    private int queueNo;

    private BigDecimal startTime;

    private BigDecimal endTime;

    public boolean isAllWindowExceedMaxWait() {
        return allWindowExceedMaxWait;
    }

    public void setAllWindowExceedMaxWait(boolean allWindowExceedMaxWait) {
        this.allWindowExceedMaxWait = allWindowExceedMaxWait;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getWindowNo() {
        return windowNo;
    }

    public void setWindowNo(String windowNo) {
        this.windowNo = windowNo;
    }

    public int getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(int queueNo) {
        this.queueNo = queueNo;
    }

    public BigDecimal getStartTime() {
        return startTime;
    }

    public void setStartTime(BigDecimal startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getEndTime() {
        return endTime;
    }

    public void setEndTime(BigDecimal endTime) {
        this.endTime = endTime;
    }

}
