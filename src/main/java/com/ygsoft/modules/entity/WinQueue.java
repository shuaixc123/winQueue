package com.ygsoft.modules.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity  //代表此类为一个表的映射entity类
@Table(name="t_win_queue")  //设置对应的表名
public class WinQueue implements Serializable {

    @Id
    @Column(name="gid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int gid;

    @Column(name="windowno")
    private String windowno;

    @Column(name="employeeno")
    private String employeeno;

    @Column(name="tasknum")
    private int tasknum;

    @Column(name="starttime")
    private BigDecimal starttime;

    @Column(name="endtime")
    private BigDecimal endtime;

    @Column(name="queueno")
    private int queueno;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getTasknum() {
        return tasknum;
    }

    public void setTasknum(int tasknum) {
        this.tasknum = tasknum;
    }

    public BigDecimal getStarttime() {
        return starttime;
    }

    public void setStarttime(BigDecimal starttime) {
        this.starttime = starttime;
    }

    public BigDecimal getEndtime() {
        return endtime;
    }

    public void setEndtime(BigDecimal endtime) {
        this.endtime = endtime;
    }

    public int getQueueno() {
        return queueno;
    }

    public void setQueueno(int queueno) {
        this.queueno = queueno;
    }

    public String getWindowno() {
        return windowno;
    }

    public void setWindowno(String windowno) {
        this.windowno = windowno;
    }

    public String getEmployeeno() {
        return employeeno;
    }

    public void setEmployeeno(String employeeno) {
        this.employeeno = employeeno;
    }

}
