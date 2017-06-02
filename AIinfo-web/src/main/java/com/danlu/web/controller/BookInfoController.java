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
import com.danlu.dleye.core.BookListManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.UserSignStatisticsManager;
import com.danlu.dleye.core.util.DleyeSwith;
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
    private UserSignStatisticsManager userSignStatisticsManager;
    @Autowired
    private DleyeSwith dleyeSwith;

    @RequestMapping("booklist.html")
    public ModelAndView getBookList(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            int dateNum = dleyeSwith.getBookListDate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", dateNum);
            List<BookList> list = bookListManager.getBookListsByParams(map);
            if (!CollectionUtils.isEmpty(list)) {
                m.addObject("bookLists", list);
            }
            m.addObject("userName", userName);
            m.addObject("bookListDate", dateNum);
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
