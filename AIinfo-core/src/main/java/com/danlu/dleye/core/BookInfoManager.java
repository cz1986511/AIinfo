package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.BookInfo;

public interface BookInfoManager {

    int addBookInfo(BookInfo bookInfo);

    int updateBookInfo(BookInfo bookInfo);

    int delBookInfo(Long id);

    BookInfo getBookInfoById(Long id);

    List<BookInfo> getBookInfoList(Map<String, Object> map);

}
