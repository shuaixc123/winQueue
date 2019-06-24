package com.ygsoft.modules.dao;

import com.ygsoft.modules.entity.WinPause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWinPauseDao extends JpaRepository<WinPause, Integer> {
}
