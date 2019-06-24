package com.ygsoft.modules.dao;

import com.ygsoft.modules.entity.SysInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysInfoDao extends JpaRepository<SysInfo, Integer> {

    @Modifying
    @Query("update SysInfo s set s.locknum=1 where s.locknum=0")
    Integer lockNum();

    @Modifying
    @Query("update SysInfo s set s.locknum=0")
    Integer unlockNum();

}
