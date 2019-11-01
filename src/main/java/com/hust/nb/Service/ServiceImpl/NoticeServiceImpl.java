package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.NoticeDao;


import com.hust.nb.Entity.Notice;
import com.hust.nb.Service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService
{
    @Autowired
    NoticeDao noticeDao;

    @Override
    public void saveNotice(Notice notice)
    {
        noticeDao.save(notice);
    }



    @Override
    public Page<Notice> getByEnprNo(String enprNo, Pageable pageable )
    {
        return noticeDao.findAllByEnprNo(enprNo,pageable);

    }

    @Override
    public Notice getNoticeById(Integer id)
    {
        return noticeDao.findNoticeById(id);
    }

    @Override
    public void deleteNotice(Notice notice)
    {
        noticeDao.delete(notice);
    }


}

