package com.ygsoft.modules.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity  //代表此类为一个表的映射entity类
@Table(name="t_win_pause")  //设置对应的表名
public class WinPause {

    @Id
    @Column(name="gid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int gid;

    @Column(name="windowno")
    private String windowno;

    @Column(name="starttime")
    private BigDecimal starttime;

    @Column(name="endtime")
    private BigDecimal endtime;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
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

    public String getWindowno() {
        return windowno;
    }

    public void setWindowno(String windowno) {
        this.windowno = windowno;
    }

}
