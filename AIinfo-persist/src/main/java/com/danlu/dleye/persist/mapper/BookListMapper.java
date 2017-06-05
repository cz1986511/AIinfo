package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookList;

public interface BookListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookList bookList);

    int insertSelective(BookList bookList);

    BookList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BookList bookList);

    int updateByPrimaryKey(BookList bookList);

    List<BookList> selectBookListsByParams(Map<String, Object> map);
}