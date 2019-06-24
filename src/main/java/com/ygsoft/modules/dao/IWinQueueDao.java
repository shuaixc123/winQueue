package com.ygsoft.modules.dao;

import com.ygsoft.modules.entity.WinQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWinQueueDao extends JpaRepository<WinQueue, Integer> {

    WinQueue findByEmployeeno(String employeeno);

    @Query(" from WinQueue q where (q.windowno, q.queueno) in (select windowno, max(queueno) from WinQueue group by windowno)")
    List<WinQueue> findMaxQueue();
}
