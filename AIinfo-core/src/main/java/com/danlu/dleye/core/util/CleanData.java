package com.danlu.dleye.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.danlu.dleye.persist.base.ArticleInfo;

public class CleanData {

    private static Logger logger = LoggerFactory.getLogger(CleanData.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;
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
}
