package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookList;

public interface BookListManager {

    int addBookList(BookList bookList);

    BookList getBookListById(Long id);

    List<BookList> getBookListsByParams(Map<String, Object> map);

    int deleteBookListById(Long id);

    int updateBookList(BookList bookList);

}
