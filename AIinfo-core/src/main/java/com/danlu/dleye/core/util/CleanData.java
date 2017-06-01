package com.danlu.dleye.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.core.BookListManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.UserSignStatisticsManager;
import com.danlu.dleye.persist.base.ArticleInfo;
import com.danlu.dleye.persist.base.BookList;
import com.danlu.dleye.persist.base.UserSign;
import com.danlu.dleye.persist.base.UserSignStatistics;

public class CleanData {

    private static Logger logger = LoggerFactory.getLogger(CleanData.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;
    @Autowired
    private BookListManager bookListManager;
    @Autowired
    private UserSignManager userSignManager;
    @Autowired
    private UserSignStatisticsManager userSignStatisticsManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    public void cleanData() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, dleyeSwith.getDefaultDate());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createTime", calendar.getTime());
            List<ArticleInfo> resultArticleInfoList = articleInfoManager
                .getArticleInfosByGmtCreate(map);
            if (!CollectionUtils.isEmpty(resultArticleInfoList)) {
                Iterator<ArticleInfo> iterator = resultArticleInfoList.iterator();
                while (iterator.hasNext()) {
                    articleInfoManager.deleteArticleInfoById(iterator.next().getId());
                }
            }
        } catch (Exception e) {
            logger.error("cleanData is exception:" + e.toString());
        }
        updateUserAddressList();
        makeLunchInfo();
        makeBookList();
        statisticsUserSign();
    }

    @SuppressWarnings("resource")
    public void updateUserAddressList() {
        try {
            String filePath = dleyeSwith.getFilePath();
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            int sheetRows = sheet.getPhysicalNumberOfRows();
            if (sheetRows > 3) {
                List<UserBaseInfo> resultList = new ArrayList<UserBaseInfo>();
                for (int r = 2; r < sheetRows; r++) {
                    Row row = sheet.getRow(r);
                    if (null != row) {
                        try {
                            UserBaseInfo userBaseInfo = new UserBaseInfo();
                            Cell cell0 = row.getCell(0);
                            userBaseInfo.setId(Long.valueOf((long) cell0.getNumericCellValue()));
                            Cell cell1 = row.getCell(1);
                            userBaseInfo.setUserName(cell1.getStringCellValue());
                            Cell cell2 = row.getCell(2);
                            userBaseInfo.setSexy(cell2.getStringCellValue());
                            Cell cell3 = row.getCell(3);
                            userBaseInfo.setBirthday(cell3.getDateCellValue().toString());
                            Cell cell4 = row.getCell(4);
                            userBaseInfo.setTelPhone(String.valueOf((long) cell4
                                .getNumericCellValue()));
                            Cell cell5 = row.getCell(5);
                            userBaseInfo.setPost(cell5.getStringCellValue());
                            resultList.add(userBaseInfo);
                        } catch (Exception e) {
                            logger.error("updateUserAddressList is exception:" + e.toString());
                        }
                    }
                }
                redisClient.set("user_address_list", resultList, 86400);
            }
        } catch (Exception e) {
            logger.error("updateUserAddressList is exception:" + e.toString());
        }
    }

    public void makeLunchInfo() {
        String lunchString = dleyeSwith.getLunchs();
        if (!StringUtils.isBlank(lunchString)) {
            String[] lunchs = lunchString.split(",");
            for (int i = 0; i < lunchs.length; i++) {
                try {
                    String defaultKey = "lunch" + i;
                    redisClient.set(defaultKey, lunchs[i], 86400);
                } catch (Exception e) {
                    logger.error("makeLunchInfo is exception:" + e.toString());
                }
            }
        }
    }

    private void makeBookList() {
        if (dleyeSwith.getMakeBookList()) {
            int bookListDate = dleyeSwith.getBookListDate();
            String memberString = dleyeSwith.getMember();
            if (null != memberString) {
                String[] members = memberString.split(",");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("date", bookListDate);
                for (int i = 0; i < members.length; i++) {
                    try {
                        String userName = members[i];
                        map.put("userName", userName);
                        List<BookList> list = bookListManager.getBookListsByParams(map);
                        if (CollectionUtils.isEmpty(list)) {
                            BookList bookList = new BookList();
                            bookList.setUserName(userName);
                            bookList.setDate(bookListDate);
                            bookList.setStatus("0");
                            bookListManager.addBookList(bookList);
                        }
                    } catch (Exception e) {
                        logger.error("makeBookList is exception:" + e.toString());
                    }
                }
            }
        }
    }

    private void statisticsUserSign() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date createStartTime = null;
        Date createEndTime = null;
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            createStartTime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            createEndTime = calendar.getTime();
        } else {
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            createEndTime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            createStartTime = calendar.getTime();
        }
        String type = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);
        map.put("type", type);
        String memberString = dleyeSwith.getMember();
        if (null != memberString) {
            String[] members = memberString.split(",");
            for (int i = 0; i < members.length; i++) {
                try {
                    String userName = members[i];
                    map.put("userName", userName);
                    List<UserSign> list = userSignManager.getUserSignListByParams(map);
                    if (!CollectionUtils.isEmpty(list)) {
                        int signNum = list.size();
                        List<UserSignStatistics> list1 = userSignStatisticsManager
                            .getUserSignStatisticsList(map);
                        if (!CollectionUtils.isEmpty(list1)) {
                            UserSignStatistics userSignStatistics = list1.get(0);
                            if (signNum != userSignStatistics.getSignNum()) {
                                userSignStatistics.setSignNum(signNum);
                                userSignStatisticsManager
                                    .updateUserSignStatistics(userSignStatistics);
                            }
                        } else {
                            UserSignStatistics newUserSignStatistics = new UserSignStatistics();
                            newUserSignStatistics.setUserName(userName);
                            newUserSignStatistics.setType(type);
                            newUserSignStatistics.setSignNum(signNum);
                            userSignStatisticsManager.addUserSignStatistics(newUserSignStatistics);
                        }
                    }
                } catch (Exception e) {
                    logger.error("statisticsUserSign is exception:" + e.toString());
                }
            }
        }
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
            calendar.add(Calendar.MONTH, 1);
        }
        System.out.println(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH));
    }
}
