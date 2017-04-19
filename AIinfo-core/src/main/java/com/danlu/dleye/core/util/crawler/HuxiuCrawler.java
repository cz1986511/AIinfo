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
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class HuxiuCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HuxiuCrawler.class);
    private static final String HUXIU = "虎嗅";
    private static final String URL_STRING = "http://www.huxiu.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public HuxiuCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Calendar calendar = Calendar.getInstance();
            String xPath = "//div[@class='mod-b mod-art ']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(HUXIU);
                    HtmlDivision division = ite.next();
                    List<HtmlAnchor> tagAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='column-link-box']/a");
                    if (!CollectionUtils.isEmpty(tagAnchorList)) {
                        articleInfo.setTag(tagAnchorList.get(0).asText());
                    }
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='mob-ctt']/h2/a");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                        articleInfo.setLinkUrl(URL_STRING
                                               + titleAnchorList.get(0).getAttribute("href"));
                    }
                    List<HtmlImage> picImageList = (List<HtmlImage>) division
                        .getByXPath(".//div[@class='mod-thumb']/a/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("data-original"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlSpan> authorSpanList = (List<HtmlSpan>) division
                        .getByXPath(".//div[@class='mob-ctt']/div[@class='mob-author']/a/span[@class='author-name ']");
                    if (!CollectionUtils.isEmpty(authorSpanList)) {
                        articleInfo.setAuthor(authorSpanList.get(0).asText());
                    }
                    List<HtmlSpan> timeSpanList = (List<HtmlSpan>) division
                        .getByXPath(".//div[@class='mob-ctt']/div[@class='mob-author']/span[@class='time']");
                    if (!CollectionUtils.isEmpty(timeSpanList)) {
                        String time = timeSpanList.get(0).asText();
                        String date = "";
                        String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
                            .get(Calendar.MONTH) + 1)) : ("0" + (calendar.get(Calendar.MONTH) + 1));
                        if ("1天前".equals(time)) {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + (calendar.get(Calendar.DAY_OF_MONTH) - 1);
                        } else if ("2天前".equals(time)) {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + (calendar.get(Calendar.DAY_OF_MONTH) - 2);
                        } else {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + calendar.get(Calendar.DAY_OF_MONTH);
                        }
                        articleInfo.setDate(date);
                    }
                    List<HtmlDivision> descDivisionList = (List<HtmlDivision>) division
                        .getByXPath(".//div[@class='mob-ctt']/div[@class='mob-sub']");
                    if (!CollectionUtils.isEmpty(descDivisionList)) {
                        articleInfo.setIntroduction(descDivisionList.get(0).asText());
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("huxiuCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("huxiuCrawler is exception:" + e.toString());
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
