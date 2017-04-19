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
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class QdailyCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QdailyCrawler.class);
    private static final String QDAILY = "好奇心日报";
    private static final String URL_STRING = "http://www.qdaily.com/tags/29.html";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public QdailyCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//div[@class='packery-item article']/a";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlAnchor> list = (List<HtmlAnchor>) page.getByXPath(xPath);
            Iterator<HtmlAnchor> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(QDAILY);
                    HtmlAnchor anchor = ite.next();
                    articleInfo.setLinkUrl("http://www.qdaily.com" + anchor.getAttribute("href"));
                    List<HtmlImage> picImageList = (List<HtmlImage>) anchor
                        .getByXPath(".//div[@class='grid-article-hd']/div[@class='pic imgcover']/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("data-src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlHeading3> h3TitleList = (List<HtmlHeading3>) anchor
                        .getByXPath(".//div[@class='grid-article-bd']/h3");
                    if (!CollectionUtils.isEmpty(h3TitleList)) {
                        articleInfo.setTitle(h3TitleList.get(0).asText());
                    }
                    List<HtmlSpan> timeSpanList = (List<HtmlSpan>) anchor
                        .getByXPath(".//div[@class='grid-article-ft clearfix']/span");
                    if (!CollectionUtils.isEmpty(timeSpanList)) {
                        articleInfo.setDate(timeSpanList.get(0).getAttribute("data-origindate")
                            .substring(0, 19));
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("qdailyCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("qdailyCrawler is exception:" + e.toString());
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
