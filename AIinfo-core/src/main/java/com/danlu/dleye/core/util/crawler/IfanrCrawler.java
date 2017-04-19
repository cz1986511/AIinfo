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
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;

public class IfanrCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(IfanrCrawler.class);
    private static final String IFANR = "爱范儿";
    private static final String URL_STRING = "http://www.ifanr.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public IfanrCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Calendar calendar = Calendar.getInstance();
            String xPath = "//div[@class='article-item article-item--card ']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(IFANR);
                    HtmlDivision division = ite.next();
                    List<HtmlAnchor> tagAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//a[@class='article-label']");
                    if (!CollectionUtils.isEmpty(tagAnchorList)) {
                        articleInfo.setTag(tagAnchorList.get(0).asText());
                    }
                    List<HtmlAnchor> urlAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//a[@class!='article-label']");
                    if (!CollectionUtils.isEmpty(urlAnchorList)) {
                        articleInfo.setLinkUrl(urlAnchorList.get(0).getAttribute("href"));
                    }
                    List<HtmlHeading3> titleH3List = (List<HtmlHeading3>) division
                        .getByXPath(".//h3");
                    if (!CollectionUtils.isEmpty(titleH3List)) {
                        articleInfo.setTitle(titleH3List.get(0).asText());
                    }
                    List<HtmlUnknownElement> timeSpanList = (List<HtmlUnknownElement>) division
                        .getByXPath(".//time");
                    if (!CollectionUtils.isEmpty(timeSpanList)) {
                        String time = timeSpanList.get(0).asText();
                        String date = "";
                        String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
                            .get(Calendar.MONTH) + 1)) : ("0" + (calendar.get(Calendar.MONTH) + 1));
                        if (time.contains("昨天")) {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + (calendar.get(Calendar.DAY_OF_MONTH) - 1);
                        } else if (time.contains("前天")) {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + (calendar.get(Calendar.DAY_OF_MONTH) - 2);
                        } else {
                            date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
                                   + calendar.get(Calendar.DAY_OF_MONTH);
                        }
                        articleInfo.setDate(date);
                    }
                    List<HtmlDivision> picDivisionList = (List<HtmlDivision>) division
                        .getByXPath(".//div[@class='article-image cover-image']");
                    if (!CollectionUtils.isEmpty(picDivisionList)) {
                        String picUrls = picDivisionList.get(0).getAttribute("style");
                        String[] picUrlArray = picUrls.split("'");
                        articleInfo.setPicUrl(picUrlArray[1]);
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("ifanrCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("ifanrCrawler is exception:" + e.toString());
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
