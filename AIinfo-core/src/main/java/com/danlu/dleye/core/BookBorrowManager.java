package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookBorrow;

public interface BookBorrowManager {

    int addBookBorrow(BookBorrow bookBorrow);

    int updateBookBorrow(BookBorrow bookBorrow);

    int delBookBorrow(Long id);

    BookBorrow getBookBorrowById(Long id);

    List<BookBorrow> getBookBorrowsByParams(Map<String, Object> map);

}
