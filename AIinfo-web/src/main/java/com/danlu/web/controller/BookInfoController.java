package com.danlu.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.core.BookBorrowManager;
import com.danlu.dleye.core.BookInfoManager;
import com.danlu.dleye.core.BookListManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.UserSignStatisticsManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.BookBorrow;
import com.danlu.dleye.persist.base.BookInfo;
import com.danlu.dleye.persist.base.BookList;
import com.danlu.dleye.persist.base.UserSign;
import com.danlu.dleye.persist.base.UserSignStatistics;

@Controller
public class BookInfoController implements Serializable {

    private static final long serialVersionUID = -9085366251L;

    private static Logger logger = LoggerFactory.getLogger(BookInfoController.class);

    @Autowired
    private UserSignManager userSignManager;
    @Autowired
    private BookListManager bookListManager;
    @Autowired
    private BookInfoManager bookInfoManager;
    @Autowired
    private BookBorrowManager bookBorrowManager;
    @Autowired
    private UserSignStatisticsManager userSignStatisticsManager;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private DleyeSwith dleyeSwith;

    @RequestMapping("books.html")
    public ModelAndView getBooks(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            int userTpye = (int) request.getSession().getAttribute("type");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", userName);
            List<BookInfo> list = bookInfoManager.getBookInfoList(map);
            if (!CollectionUtils.isEmpty(list)) {
                m.addObject("books", list);
            }
            m.addObject("userType", userTpye);
            m.addObject("userName", userName);
        } catch (Exception e) {
            logger.info("getBooks is exception:" + e.toString());
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("addbook.action")
    @ResponseBody
    public String addBookAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String bookName = request.getParameter("bookName");
        String author = request.getParameter("author");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        if (StringUtils.isBlank(bookName)) {
            result.put("status", false);
            result.put("msg", "bookName is blank");
        } else {
            try {
                String userName = (String) request.getSession().getAttribute("userName");
                int userType = (int) request.getSession().getAttribute("type");
                if (!StringUtils.isBlank(userName) && userType == 1) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("bookName", bookName);
                    List<BookInfo> list = bookInfoManager.getBookInfoList(map);
                    if (!CollectionUtils.isEmpty(list)) {
                        result.put("status", false);
                        result.put("msg", "add book repeate");
                    } else {
                        BookInfo bookInfo = new BookInfo();
                        bookInfo.setBookName(bookName);
                        bookInfo.setBookAuthor(author);
                        bookInfo.setBookType(type);
                        bookInfo.setBookSource(source);
                        int add = bookInfoManager.addBookInfo(bookInfo);
                        if (add > 0) {
                            result.put("status", true);
                            result.put("msg", dleyeSwith.getWisdom());
                            logger.info("addBook:" + bookName + "sucess by " + userName);
                        } else {
                            result.put("status", false);
                            result.put("msg", "add book faild");
                            logger.info("addBook:" + bookName + "faild by " + userName);
                        }
                    }
                } else {
                    result.put("status", false);
                    result.put("msg", "add book faild");
                }
            } catch (Exception e) {
                logger.error("addBookAction is exception:" + e.toString());
                result.put("status", false);
                result.put("msg", "add book exception");
            }
        }
        return json.toJSONString();
    }

    @RequestMapping("bookborrow.action")
    @ResponseBody
    public String bookBorrowAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String id = request.getParameter("id");
        if (StringUtils.isBlank(id)) {
            result.put("status", false);
            result.put("msg", "bookId is blank");
        } else {
            try {
                String userName = (String) request.getSession().getAttribute("userName");
                if (!StringUtils.isBlank(userName)) {
                    BookInfo book = bookInfoManager.getBookInfoById(Long.valueOf(id));
                    if (null != book && "01".equals(book.getBookStatus())) {
                        String defaultKey = "borrow-" + userName;
                        Integer remainder = (Integer) redisClient.get(defaultKey,
                            new TypeReference<Integer>() {
                            });
                        if (null != remainder && remainder > 0) {
                            BookBorrow borrow = new BookBorrow();
                            borrow.setBookId(book.getBookId());
                            borrow.setBookName(book.getBookName());
                            borrow.setUserName(userName);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            Date startTimeDate = calendar.getTime();
                            calendar.add(Calendar.DATE, dleyeSwith.getBorrowDate());
                            Date endTimeDate = calendar.getTime();
                            borrow.setStartTime(startTimeDate);
                            borrow.setEndTime(endTimeDate);
                            borrow.setStatus("01");
                            int addNum = bookBorrowManager.addBookBorrow(borrow);
                            if (addNum > 0) {
                                redisClient.set(defaultKey, remainder - 1, 60 * 24 * 60 * 60);
                                result.put("status", true);
                                result.put("msg", dleyeSwith.getWisdom());
                                BookInfo nBookInfo = new BookInfo();
                                nBookInfo.setBookId(book.getBookId());
                                nBookInfo.setBookStatus("02");
                                bookInfoManager.updateBookInfo(nBookInfo);
                            } else {
                                result.put("status", false);
                                result.put("msg", "borrow book faild");
                            }
                        } else {
                            result.put("status", false);
                            result.put("msg", "borrow book faild");
                        }
                    } else {
                        result.put("status", false);
                        result.put("msg", "book status is wrong");
                    }
                } else {
                    result.put("status", false);
                    result.put("msg", "add book faild");
                }
            } catch (Exception e) {
                logger.error("addBookAction is exception:" + e.toString());
                result.put("status", false);
                result.put("msg", "add book exception");
            }
        }
        return json.toJSONString();
    }

    @RequestMapping("bookborrow.html")
    public ModelAndView getBookBorrow(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", "01");
            List<BookBorrow> resultList = bookBorrowManager.getBookBorrowsByParams(map);
            m.addObject("bookBorrows", resultList);
            m.addObject("userName", userName);
        } catch (Exception e) {
            logger.info("getBookBorrow is exception:" + e.toString());
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("modifyborrow.action")
    @ResponseBody
    public String modifyBorrow(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(status)) {
            result.put("status", false);
            result.put("msg", "id or status is null");
        } else {
            try {
                String userName = (String) request.getSession().getAttribute("userName");
                int userType = (int) request.getSession().getAttribute("type");
                if (!StringUtils.isBlank(userName) && userType == 1) {
                    BookBorrow bookBorrow = new BookBorrow();
                    bookBorrow.setId(Long.valueOf(id));
                    bookBorrow.setStatus(status);
                    int temp = bookBorrowManager.updateBookBorrow(bookBorrow);
                    if (temp > 0) {
                        BookBorrow nBookBorrow = bookBorrowManager.getBookBorrowById(Long
                            .valueOf(id));
                        BookInfo bookInfo = new BookInfo();
                        bookInfo.setBookId(nBookBorrow.getBookId());
                        if ("02".equals(status)) {
                            bookInfo.setBookStatus("03");
                        } else {
                            String defaultKey = "borrow-" + nBookBorrow.getUserName();
                            Integer remainder = (Integer) redisClient.get(defaultKey,
                                new TypeReference<Integer>() {
                                });
                            redisClient.set(defaultKey, remainder + 1, 60 * 24 * 60 * 60);
                            bookInfo.setBookStatus("01");
                        }
                        bookInfoManager.updateBookInfo(bookInfo);
                        result.put("status", true);
                        result.put("msg", dleyeSwith.getWisdom());
                    } else {
                        result.put("status", false);
                        result.put("msg", "modifyBorrow is faild");
                    }
                } else {
                    result.put("status", false);
                    result.put("msg", "no promit to do");
                }
            } catch (Exception e) {
                logger.error("modifyBorrow is exception:" + e.toString());
                result.put("status", false);
                result.put("msg", "modifyBorrow is exception");
            }
        }
        return json.toJSONString();
    }

    @RequestMapping("booklist.html")
    public ModelAndView getBookList(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            int userType = (int) request.getSession().getAttribute("type");
            int dateNum = dleyeSwith.getBookListDate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", dateNum);
            List<BookList> list = bookListManager.getBookListsByParams(map);
            if (!CollectionUtils.isEmpty(list)) {
                m.addObject("bookLists", list);
            }
            m.addObject("userName", userName);
            m.addObject("bookListDate", dateNum);
            m.addObject("userType", userType);
        } catch (Exception e) {
            logger.info("getBookList is exception:" + e.toString());
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("modify_book.action")
    @ResponseBody
    public String modifyBookAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String unreadString = request.getParameter("unread");
        String readString = request.getParameter("read");
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            if (!StringUtils.isBlank(userName)) {
                int date = dleyeSwith.getBookListDate();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("date", date);
                map.put("userName", userName);
                List<BookList> list = bookListManager.getBookListsByParams(map);
                int flag = 0;
                if (!CollectionUtils.isEmpty(list)) {
                    BookList bookList = list.get(0);
                    if (!StringUtils.isBlank(unreadString)) {
                        bookList.setUnreadList(unreadString);
                    }
                    if (!StringUtils.isBlank(readString)) {
                        bookList.setReadList(readString);
                    }
                    flag = bookListManager.updateBookList(bookList);
                } else {
                    BookList bookList = new BookList();
                    bookList.setUserName(userName);
                    bookList.setDate(date);
                    bookList.setStatus("0");
                    if (!StringUtils.isBlank(unreadString)) {
                        bookList.setUnreadList(unreadString);
                    }
                    if (!StringUtils.isBlank(readString)) {
                        bookList.setReadList(readString);
                    }
                    flag = bookListManager.addBookList(bookList);
                }
                if (flag != 0) {
                    result.put("status", true);
                } else {
                    result.put("status", false);
                    result.put("msg", "modify_book error!");
                }
            } else {
                result.put("status", false);
                result.put("msg", "modify_book error!");
            }
        } catch (Exception e) {
            logger.error("modify_book is exception:" + e.toString());
            result.put("status", false);
            result.put("msg", "modify_book exception!");
        }
        return json.toJSONString();
    }

    @RequestMapping("usersign.html")
    public ModelAndView userSign(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            int userType = (int) request.getSession().getAttribute("type");
            String dateString = getDateString();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", dateString);
            List<UserSign> list = userSignManager.getUserSignListByParams(map);
            if (!CollectionUtils.isEmpty(list)) {
                m.addObject("signList", list);
            }
            String type = getMonthString();
            m.addObject("month", type);
            map.put("type", type);
            List<UserSignStatistics> list1 = userSignStatisticsManager
                .getUserSignStatisticsList(map);
            if (!CollectionUtils.isEmpty(list1)) {
                m.addObject("signStatistics", list1);
            }
            m.addObject("userType", userType);
        } catch (Exception e) {
            logger.error("userSign is exception:" + e.toString());
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("sign.action")
    @ResponseBody
    public String signAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String signInfo = request.getParameter("signInfo");
        if (StringUtils.isBlank(signInfo)) {
            result.put("status", false);
        } else {
            try {
                String userName = (String) request.getSession().getAttribute("userName");
                if (!StringUtils.isBlank(userName)) {
                    String dateString = getDateString();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("userName", userName);
                    map.put("date", dateString);
                    List<UserSign> list = userSignManager.getUserSignListByParams(map);
                    if (!CollectionUtils.isEmpty(list)) {
                        result.put("status", false);
                    } else {
                        UserSign userSign = new UserSign();
                        userSign.setSignInfo(signInfo);
                        userSign.setUserName(userName);
                        userSign.setDate(getDateString());
                        userSignManager.addUserSign(userSign);
                        result.put("status", true);
                        logger.info("user:" + userName + "|sign:" + signInfo);
                    }
                }
            } catch (Exception e) {
                logger.error("signAction is exception:" + e.toString());
                result.put("status", false);
            }
        }
        result.put("msg", dleyeSwith.getWisdom());
        return json.toJSONString();
    }

    private String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    private String getMonthString() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            calendar.add(Calendar.MONTH, 1);
        }
        return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
    }

}
