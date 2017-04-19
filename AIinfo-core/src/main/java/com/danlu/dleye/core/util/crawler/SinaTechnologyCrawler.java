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

public class SinaTechnologyCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SinaTechnologyCrawler.class);
    private static final String SINA_TECHNOLOGY = "新浪科技";
    private static final String URL_STRING = "http://tech.sina.com.cn";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public SinaTechnologyCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Calendar calendar = Calendar.getInstance();
            String xPath = "//div[@class='feed-card-item']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(SINA_TECHNOLOGY);
                    HtmlDivision division = ite.next();
                    List<HtmlImage> picImageList = (List<HtmlImage>) division
                        .getByXPath(".//div[@class='feed-card-img']/a/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("data-src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='tech-feed-right']/h2/a");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setLinkUrl(titleAnchorList.get(0).getAttribute("href"));
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                    }
                    List<HtmlAnchor> tagAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='tech-feed-right']/div[@class='feed-card-tags tech-feed-card-tags']/span[@class='feed-card-tags-col']/a");
                    if (!CollectionUtils.isEmpty(tagAnchorList)) {
                        articleInfo.setTag(tagAnchorList.get(0).asText());
                    }
                    List<HtmlDivision> timeDivisionList = (List<HtmlDivision>) division
                        .getByXPath(".//div[@class='tech-feed-right']/div[@class='feed-card-a feed-card-clearfix']/div[@class='feed-card-time']");
                    if (!CollectionUtils.isEmpty(timeDivisionList)) {
                        String time = timeDivisionList.get(0).asText();
                        String date = "";
                        String[] timeArray = time.split(" ");
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
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("sinaTechnologyCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("sinaTechnologyCrawler is exception:" + e.toString());
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
