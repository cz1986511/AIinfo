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
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class TmtPostCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TmtPostCrawler.class);
    private static final String TMTPOST = "钛媒体";
    private static final String URL_STRING = "http://www.tmtpost.com";
    private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

    private WebClient webClient;
    private ArticleInfoManager articleInfoManager;

    public TmtPostCrawler(ArticleInfoManager articleInfoManager) {
        super();
        this.webClient = initWebClient();
        this.articleInfoManager = articleInfoManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            String xPath = "//li[@class='post_part clear']";
            HtmlPage page = webClient.getPage(URL_STRING);
            List<HtmlListItem> list = (List<HtmlListItem>) page.getByXPath(xPath);
            Iterator<HtmlListItem> ite = list.iterator();
            while (ite.hasNext()) {
                try {
                    ArticleInfo articleInfo = new ArticleInfo();
                    articleInfo.setSource(TMTPOST);
                    HtmlListItem listItem = ite.next();
                    List<HtmlImage> picImageList = (List<HtmlImage>) listItem
                        .getByXPath(".//a[@class='pic']/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("src"));
                    } else {
                        articleInfo.setPicUrl(DEFAULT_PIC);
                    }
                    List<HtmlAnchor> titleAnchorList = (List<HtmlAnchor>) listItem
                        .getByXPath(".//div[@class='cont']/h3/a[@class='title']");
                    if (!CollectionUtils.isEmpty(titleAnchorList)) {
                        articleInfo.setLinkUrl(URL_STRING
                                               + titleAnchorList.get(0).getAttribute("href"));
                        articleInfo.setTitle(titleAnchorList.get(0).asText());
                    }
                    List<HtmlSpan> dateSpanList = (List<HtmlSpan>) listItem
                        .getByXPath(".//div[@class='cont']/div[@class='info']/span[@class='author']");
                    if (!CollectionUtils.isEmpty(dateSpanList)) {
                        String dataString = dateSpanList.get(0).asText();
                        String[] aStrings = dataString.split("•");
                        articleInfo.setAuthor(aStrings[0]);
                        articleInfo.setDate(aStrings[1]);
                    }
                    List<HtmlParagraph> descParagraphList = (List<HtmlParagraph>) listItem
                        .getByXPath(".//div[@class='cont']/div[@class='info']/p[@class='summary']");
                    if (!CollectionUtils.isEmpty(descParagraphList)) {
                        articleInfo.setIntroduction(descParagraphList.get(0).asText());
                    }
                    List<HtmlDivision> tagDivisionList = (List<HtmlDivision>) listItem
                        .getByXPath(".//div[@class='cont']/div[@class='tag']");
                    if (!CollectionUtils.isEmpty(tagDivisionList)) {
                        articleInfo.setTag(tagDivisionList.get(0).asText().replace("，", " "));
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("tmtPostCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("tmtPostCrawler is exception:" + e.toString());
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
