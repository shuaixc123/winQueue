package com.ygsoft.modules.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity  //代表此类为一个表的映射entity类
@Table(name="t_win_info")  //设置对应的表名
public class WinInfo implements Serializable {

    @Id
    @Column(name="gid",length=9)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int gid;

    @Column(name="windowno")
    private String windowno;

    @Column(name="busitime")
    private BigDecimal busitime;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public BigDecimal getBusitime() {
        return busitime;
    }

    public void setBusitime(BigDecimal busitime) {
        this.busitime = busitime;
    }


    public String getWindowno() {
        return windowno;
    }

    public void setWindowno(String windowno) {
        this.windowno = windowno;
    }
}
