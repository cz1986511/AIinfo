package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookBorrow;

public interface BookBorrowMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookBorrow bookBorrow);

    int insertSelective(BookBorrow bookBorrow);

    BookBorrow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BookBorrow bookBorrow);

    int updateByPrimaryKey(BookBorrow bookBorrow);

    List<BookBorrow> selectBookBorrowsByParams(Map<String, Object> map);
}