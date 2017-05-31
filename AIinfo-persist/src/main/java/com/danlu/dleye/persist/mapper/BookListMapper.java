package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookList;

public interface BookListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookList record);

    int insertSelective(BookList record);

    BookList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BookList record);

    int updateByPrimaryKey(BookList record);

    List<BookList> selectBookListsByParams(Map<String, Object> map);
}