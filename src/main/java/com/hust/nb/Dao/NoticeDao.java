package com.hust.nb.Dao;

import com.hust.nb.Entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface NoticeDao extends JpaRepository<Notice,Notice>, JpaSpecificationExecutor<Notice>
{

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_notice where enprNo = ?1 order by id desc")
    Page<Notice> findAllByEnprNo(String enprNo, Pageable pageable);


    Notice findNoticeById(Integer id);
}
