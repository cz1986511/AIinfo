package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookInfo;

public interface BookInfoMapper {
    int deleteByPrimaryKey(Long bookId);

    int insert(BookInfo bookInfo);

    int insertSelective(BookInfo bookInfo);

    BookInfo selectByPrimaryKey(Long bookId);

    int updateByPrimaryKeySelective(BookInfo bookInfo);

    int updateByPrimaryKey(BookInfo bookInfo);

    List<BookInfo> selectBookInfosByParams(Map<String, Object> map);
}