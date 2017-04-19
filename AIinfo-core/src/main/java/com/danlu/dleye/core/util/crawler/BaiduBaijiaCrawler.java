package com.danlu.dleye.core.util.crawler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.persist.base.ArticleInfo;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class BaiduBaijiaCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BaiduBaijiaCrawler.class);
    private static final String BAIDU_BAIJIA = "百度百家";
    private static final String URL_STRING = "http://baijia.baidu.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public BaiduBaijiaCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Calendar calendar = Calendar.getInstance();
            String xPath = "//div[@class='feeds-item hasImg']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(BAIDU_BAIJIA);
                    HtmlDivision division = ite.next();
                    List<HtmlImage> iTitleList = (List<HtmlImage>) division
                        .getByXPath(".//p[@class='feeds-item-pic']/a/img");
                    if (!CollectionUtils.isEmpty(iTitleList)) {
                        articleInfo.setPicUrl(iTitleList.get(0).getAttribute("src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlAnchor> aTitleList = (List<HtmlAnchor>) division.getByXPath(".//h3/a");
                    if (!CollectionUtils.isEmpty(aTitleList)) {
                        articleInfo.setTitle(aTitleList.get(0).asText());
                        articleInfo.setLinkUrl(aTitleList.get(0).getAttribute("href"));
                    }
                    List<HtmlParagraph> pDescList = (List<HtmlParagraph>) division
                        .getByXPath(".//p[@class='feeds-item-text1']");
                    if (!CollectionUtils.isEmpty(pDescList)) {
                        articleInfo.setIntroduction(pDescList.get(0).asText());
                    }
                    List<HtmlSpan> tmSpanList = (List<HtmlSpan>) division
                        .getByXPath(".//div[@class='feeds-item-info']/span[@class='tm']");
                    if (!CollectionUtils.isEmpty(tmSpanList)) {
                        String time = tmSpanList.get(0).asText();
                        String date = "";
                        String[] timeArray = time.split("-");
                        if (timeArray.length == 1) {
                            String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
                                .get(Calendar.MONTH) + 1))
                                : ("0" + (calendar.get(Calendar.MONTH) + 1));
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + calendar.get(Calendar.DAY_OF_MONTH);
                        } else {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + time;
                        }
                        articleInfo.setDate(date);
                    }
                    List<HtmlAnchor> authorAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='feeds-item-info']/a[@class='feeds-item-author']");
                    if (!CollectionUtils.isEmpty(authorAnchorList)) {

                        articleInfo.setAuthor(authorAnchorList.get(0).asText());
                    }
                    List<HtmlAnchor> tagAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='feeds-item-info']/p[@class='labels']/span[@class='label']/a");
                    if (!CollectionUtils.isEmpty(tagAnchorList)) {
                        String tag = "";
                        Iterator<HtmlAnchor> iterator = tagAnchorList.iterator();
                        while (iterator.hasNext()) {
                            tag += iterator.next().asText() + " ";
                        }
                        articleInfo.setTag(tag);
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("baiduBaijiaCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("baiduBaijiaCrawler is exception:" + e.toString());
        }
        webClient.closeAllWindows();
    }

    private void saveArticle(ArticleInfo articleInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", articleInfo.getTitle());
        map.put("source", articleInfo.getSource());
        map.put("offset", 0);
        map.put("limit", 200);
        List<ArticleInfo> result = articleInfoManager.getArticleInfosByParams(map);
        if (CollectionUtils.isEmpty(result)) {
            articleInfoManager.addArticleInfo(articleInfo);
        }
    }

    private WebClient initWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        int timeout = webClient.getOptions().getTimeout();
        webClient.getOptions().setTimeout(timeout * 10);
        return webClient;
    }

}
