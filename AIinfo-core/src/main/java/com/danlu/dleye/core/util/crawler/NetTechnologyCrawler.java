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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class NetTechnologyCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NetTechnologyCrawler.class);
    private static final String NET_TECHNOLOGY = "网易科技";
    private static final String URL_STRING = "http://tech.163.com/internet";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public NetTechnologyCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//ul[@class='newsList']/li";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlListItem> list = (List<HtmlListItem>) page.getByXPath(xPath);
            Iterator<HtmlListItem> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(NET_TECHNOLOGY);
                    HtmlListItem division = ite.next();
                    List<HtmlAnchor> titleAchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='titleBar clearfix']/h3[@class='bigsize ']/a");
                    if (!CollectionUtils.isEmpty(titleAchorList)) {
                        articleInfo.setTitle(titleAchorList.get(0).asText());
                        articleInfo.setLinkUrl(titleAchorList.get(0).getAttribute("href"));
                    }
                    List<HtmlImage> picImageList = (List<HtmlImage>) division
                        .getByXPath(".//div[@class='clearfix']/a/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlParagraph> descParaList = (List<HtmlParagraph>) division
                        .getByXPath(".//div[@class='clearfix']/div[@class='newsDigest']/p");
                    if (!CollectionUtils.isEmpty(descParaList)) {
                        articleInfo.setIntroduction(descParaList.get(0).asText());
                    }
                    List<HtmlSpan> authorSpanList = (List<HtmlSpan>) division
                        .getByXPath(".//div[@class='newsBottom clearfix']/p[@class='sourceDate']/span");
                    if (!CollectionUtils.isEmpty(authorSpanList)) {
                        articleInfo.setAuthor(authorSpanList.get(0).asText());
                    }
                    List<HtmlParagraph> timePList = (List<HtmlParagraph>) division
                        .getByXPath(".//div[@class='newsBottom clearfix']/p[@class='sourceDate']");
                    if (!CollectionUtils.isEmpty(timePList)) {
                        articleInfo.setDate(timePList.get(0).asText()
                            .replace(authorSpanList.get(0).asText(), ""));
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("netTechnologyCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("netTechnologyCrawler is exception:" + e.toString());
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
