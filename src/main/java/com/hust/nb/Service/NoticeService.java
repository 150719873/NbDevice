package com.hust.nb.Service;

import com.hust.nb.Entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService
{
    void saveNotice(Notice notice);

    Page<Notice> getByEnprNo(String enprNo, Pageable pageable);

    Notice getNoticeById(Integer id);

    void deleteNotice(Notice notice);
}
