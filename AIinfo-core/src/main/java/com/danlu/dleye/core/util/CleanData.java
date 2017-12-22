package com.danlu.dleye.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class CleanData
{

    private static Logger logger = LoggerFactory.getLogger(CleanData.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    public void cleanData()
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, dleyeSwith.getDefaultDate());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createTime", calendar.getTime());
            List<ArticleInfo> resultArticleInfoList = articleInfoManager
                .getArticleInfosByGmtCreate(map);
            if (!CollectionUtils.isEmpty(resultArticleInfoList))
            {
                Iterator<ArticleInfo> iterator = resultArticleInfoList.iterator();
                while (iterator.hasNext())
                {
                    articleInfoManager.deleteArticleInfoById(iterator.next().getId());
                }
            }
        }
        catch (Exception e)
        {
            logger.error("cleanData is exception:" + e.toString());
        }
        updateUserAddressList();
        makeWisdom();
    }

    @SuppressWarnings("resource")
    public void updateUserAddressList()
    {
        try
        {
            String filePath = dleyeSwith.getFilePath();
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            int sheetRows = sheet.getPhysicalNumberOfRows();
            if (sheetRows > 3)
            {
                List<UserBaseInfo> resultList = new ArrayList<UserBaseInfo>();
                for (int r = 10; r < sheetRows; r++)
                {
                    Row row = sheet.getRow(r);
                    if (null != row)
                    {
                        try
                        {
                            UserBaseInfo userBaseInfo = new UserBaseInfo();
                            userBaseInfo.setId((long) r);
                            Cell cell0 = row.getCell(0);
                            userBaseInfo.setUserId(cell0.getStringCellValue());
                            Cell cell3 = row.getCell(3);
                            userBaseInfo.setUserName(cell3.getStringCellValue());
                            Cell cell4 = row.getCell(4);
                            userBaseInfo.setPost(cell4.getStringCellValue());
                            Cell cell5 = row.getCell(5);
                            userBaseInfo.setTelPhone(String.valueOf((long) cell5
                                .getNumericCellValue()));
                            resultList.add(userBaseInfo);
                            String telName = "tel-" + userBaseInfo.getUserName();
                            redisClient.set(telName, userBaseInfo.getTelPhone(), 86400);
                        }
                        catch (Exception e)
                        {
                            logger.error("updateUserAddressList is exception:" + r);
                        }
                    }
                }
                redisClient.set("user_address_list", resultList, 86400);
            }
        }
        catch (Exception e)
        {
            logger.error("updateUserAddressList is exception:" + e.toString());
        }
    }

    private void makeWisdom()
    {
        try
        {
            File file = new File("/data/file/wisdom.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                "UTF-8"));
            String s = null;
            String key = "wisdom-";
            int i = 0;
            while ((s = br.readLine()) != null)
            {
                logger.info("=====" + s);
                redisClient.set(key + i, s, 86400);
                i++;
            }
            br.close();
        }
        catch (Exception e)
        {
            logger.error("makeWisdom is exception:" + e.toString());
        }
    }

    public static void main(String[] args)
    {
    }
}
