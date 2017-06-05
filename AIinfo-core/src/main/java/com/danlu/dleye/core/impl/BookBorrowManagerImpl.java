package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.BookBorrowManager;
import com.danlu.dleye.persist.base.BookBorrow;
import com.danlu.dleye.persist.mapper.BookBorrowMapper;

public class BookBorrowManagerImpl implements BookBorrowManager {

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    public int addBookBorrow(BookBorrow bookBorrow) {
        if (null != bookBorrow) {
            return bookBorrowMapper.insertSelective(bookBorrow);
        } else {
            return 0;
        }
    }

    @Override
    public int updateBookBorrow(BookBorrow bookBorrow) {
        if (null != bookBorrow) {
            return bookBorrowMapper.updateByPrimaryKeySelective(bookBorrow);
        } else {
            return 0;
        }
    }

    @Override
    public int delBookBorrow(Long id) {
        if (null != id) {
            return bookBorrowMapper.deleteByPrimaryKey(id);
        } else {
            return 0;
        }
    }

    @Override
    public BookBorrow getBookBorrowById(Long id) {
        if (null != id) {
            return bookBorrowMapper.selectByPrimaryKey(id);
        } else {
            return null;
        }
    }

    @Override
    public List<BookBorrow> getBookBorrowsByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return bookBorrowMapper.selectBookBorrowsByParams(map);
        } else {
            return null;
        }
    }

}
