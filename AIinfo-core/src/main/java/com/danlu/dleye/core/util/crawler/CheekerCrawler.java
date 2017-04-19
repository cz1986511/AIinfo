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
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class CheekerCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CheekerCrawler.class);
    private static final String CHEEKR = "粹客网";
    private static final String URL_STRING = "http://www.cheekr.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public CheekerCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//div[@class='media']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(CHEEKR);
                    HtmlDivision division = ite.next();
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='media_text']/h2[@class='indexmediatitle']/a");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setLinkUrl(titleAnchorList.get(0).getAttribute("href"));
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                    }
                    List<HtmlParagraph> descParagraphList = (List<HtmlParagraph>) division
                        .getByXPath(".//div[@class='media_text']/span[@class='abstract']/p");
                    if (!CollectionUtils.isEmpty(descParagraphList)) {
                        articleInfo.setIntroduction(descParagraphList.get(0).asText());
                    }
                    List<HtmlAnchor> authorAnchorList = (List<HtmlAnchor>) division
                        .getByXPath(".//div[@class='media_text']/div[@class='author_and_time']/span[@class='author']/a");
                    if (!CollectionUtils.isEmpty(authorAnchorList)) {
                        articleInfo.setAuthor(authorAnchorList.get(0).asText());
                    }
                    List<HtmlSpan> tagSpanList = (List<HtmlSpan>) division
                        .getByXPath(".//div[@class='media_text']/span[@class='tags']");
                    if (!CollectionUtils.isEmpty(tagSpanList)) {
                        String tags = "";
                        Iterator<HtmlSpan> tagIterator = tagSpanList.iterator();
                        while (tagIterator.hasNext()) {
                            tags += tagIterator.next().asText() + " ";
                        }
                        articleInfo.setTag(tags);
                    }
                    List<HtmlImage> picImageList = (List<HtmlImage>) division
                        .getByXPath(".//a[@class='mediaimg_a']/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("cheekerCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("cheekerCrawler is exception:" + e.toString());
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
