package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.BookInfoManager;
import com.danlu.dleye.persist.base.BookInfo;
import com.danlu.dleye.persist.mapper.BookInfoMapper;

public class BookInfoManagerImpl implements BookInfoManager {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    public int addBookInfo(BookInfo bookInfo) {
        if (null != bookInfo) {
            return bookInfoMapper.insertSelective(bookInfo);
        } else {
            return 0;
        }
    }

    @Override
    public int updateBookInfo(BookInfo bookInfo) {
        if (null != bookInfo) {
            return bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
        } else {
            return 0;
        }
    }

    @Override
    public int delBookInfo(Long id) {
        if (null != id) {
            return bookInfoMapper.deleteByPrimaryKey(id);
        } else {
            return 0;
        }
    }

    @Override
    public BookInfo getBookInfoById(Long id) {
        if (null != id) {
            return bookInfoMapper.selectByPrimaryKey(id);
        } else {
            return null;
        }
    }

    @Override
    public List<BookInfo> getBookInfoList(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return bookInfoMapper.selectBookInfosByParams(map);
        } else {
            return null;
        }
    }

}
