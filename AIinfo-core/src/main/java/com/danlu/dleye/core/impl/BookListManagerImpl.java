package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.BookListManager;
import com.danlu.dleye.persist.base.BookList;
import com.danlu.dleye.persist.mapper.BookListMapper;

public class BookListManagerImpl implements BookListManager {

    @Autowired
    private BookListMapper bookListMapper;

    @Override
    public int addBookList(BookList bookList) {
        if (null != bookList) {
            return bookListMapper.insertSelective(bookList);
        } else {
            return 0;
        }
    }

    @Override
    public BookList getBookListById(Long id) {
        if (null != id) {
            return bookListMapper.selectByPrimaryKey(id);
        } else {
            return null;
        }
    }

    @Override
    public List<BookList> getBookListsByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return bookListMapper.selectBookListsByParams(map);
        } else {
            return null;
        }
    }

    @Override
    public int deleteBookListById(Long id) {
        if (null != id) {
            return bookListMapper.deleteByPrimaryKey(id);
        } else {
            return 0;
        }
    }

    @Override
    public int updateBookList(BookList bookList) {
        if (null != bookList) {
            return bookListMapper.updateByPrimaryKeySelective(bookList);
        } else {
            return 0;
        }
    }

}
