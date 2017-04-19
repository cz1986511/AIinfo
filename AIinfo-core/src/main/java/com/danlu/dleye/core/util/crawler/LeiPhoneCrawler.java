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
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LeiPhoneCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LeiPhoneCrawler.class);
    private static final String LEIPHONE = "雷锋网";
    private static final String URL_STRING = "http://www.leiphone.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public LeiPhoneCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//div[@class='lph-pageList index-pageList']/div[@class='list']/ul[@class='clr']/li";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlListItem> list = (List<HtmlListItem>) page.getByXPath(xPath);
            Iterator<HtmlListItem> ite = list.iterator();
            Calendar calendar = Calendar.getInstance();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(LEIPHONE);
                    HtmlListItem item = ite.next();
                    List<HtmlImage> picImageList = (List<HtmlImage>) item
                        .getByXPath(".//div[@class='img']/a/img[@class='lazy']");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("data-original"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) item
                        .getByXPath(".//div[@class='box']/div[@class='word']/h3/a");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setLinkUrl(titleAnchorList.get(0).getAttribute("href"));
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                    }
                    List<HtmlDivision> descDivisionList = (List<HtmlDivision>) item
                        .getByXPath(".//div[@class='box']/div[@class='word']/div[@class='des']");
                    if (!CollectionUtils.isEmpty(descDivisionList)) {
                        articleInfo.setIntroduction(descDivisionList.get(0).asText());
                    }
                    List<HtmlAnchor> authorAnchorList = (List<HtmlAnchor>) item
                        .getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/a");
                    if (!CollectionUtils.isEmpty(authorAnchorList)) {
                        articleInfo.setAuthor(authorAnchorList.get(0).asText());
                    }
                    List<HtmlDivision> timeDivisionList = (List<HtmlDivision>) item
                        .getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/div[@class='time']");
                    if (!CollectionUtils.isEmpty(timeDivisionList)) {
                        String time = timeDivisionList.get(0).asText();
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
                    List<HtmlAnchor> tagAnchorList = (List<HtmlAnchor>) item
                        .getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/div[@class='tags']/a");
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
                    logger.error("leiPhoneCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("leiPhoneCrawler is exception:" + e.toString());
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
