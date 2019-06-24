package com.ygsoft.modules.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity  //代表此类为一个表的映射entity类
@Table(name="t_sys_info")  //设置对应的表名
public class SysInfo implements Serializable {

    @Id
    @Column(name="gid",length=9)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int gid;

    @Column(name="locknum")
    private int locknum;

    @Column(name="maxqueue")
    private int maxqueue;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getLocknum() {
        return locknum;
    }

    public void setLocknum(int locknum) {
        this.locknum = locknum;
    }

    public int getMaxqueue() {
        return maxqueue;
    }

    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }
}
