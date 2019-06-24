package com.ygsoft.modules.dao;

import com.ygsoft.modules.entity.WinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWinInfoDao extends JpaRepository<WinInfo, Integer> {

}
