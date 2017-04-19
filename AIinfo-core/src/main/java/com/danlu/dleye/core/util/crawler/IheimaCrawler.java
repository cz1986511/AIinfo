package com.danlu.dleye.core.util.crawler;

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
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;

public class IheimaCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(IheimaCrawler.class);
    private static final String IHEIMA = "i黑马";
    private static final String URL_STRING = "http://www.iheima.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public IheimaCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//article[@class='item-wrap cf']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlUnknownElement> list = (List<HtmlUnknownElement>) page.getByXPath(xPath);
            Iterator<HtmlUnknownElement> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(IHEIMA);
                    HtmlUnknownElement listItem = ite.next();
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) listItem
                        .getByXPath(".//div[@class='desc']/div[@class='title-wrap']/a[@class='title']");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                        articleInfo.setLinkUrl(titleAnchorList.get(0).getAttribute("href"));
                    }
                    List<HtmlDivision> descDivisionList = (List<HtmlDivision>) listItem
                        .getByXPath(".//div[@class='desc']/div[@class='brief']");
                    if (!CollectionUtils.isEmpty(descDivisionList)) {
                        articleInfo.setIntroduction(descDivisionList.get(0).asText());
                    }
                    List<HtmlSpan> dateSpanList = (List<HtmlSpan>) listItem
                        .getByXPath(".//div[@class='desc']/div[@class='author']/span[@class='time-wrap fl']/span[@class='timeago']");
                    if (!CollectionUtils.isEmpty(dateSpanList)) {
                        articleInfo.setDate(dateSpanList.get(0).asText());
                    }
                    List<HtmlSpan> authorSpanList = (List<HtmlSpan>) listItem
                        .getByXPath(".//div[@class='desc']/div[@class='author']/a[@class='fr']/span[@class='name']");
                    if (!CollectionUtils.isEmpty(authorSpanList)) {
                        articleInfo.setAuthor(authorSpanList.get(0).asText());
                    }
                    List<HtmlImage> picImageList = (List<HtmlImage>) listItem
                        .getByXPath(".//div[@class='desc']/div[@class='brief hasimg']/a[@class='cf']/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("iheimaCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("iheimaCrawler is exception:" + e.toString());
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
