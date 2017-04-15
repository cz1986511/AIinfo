package com.danlu.dleye.core.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.persist.base.ArticleInfo;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;

public class Crawler {

    private static Logger logger = LoggerFactory.getLogger(Crawler.class);

    @Autowired
    private ArticleInfoManager articleInfoManager;

    private static final String BAIDU_BAIJIA = "百度百家";
    private static final String SINA_TECHNOLOGY = "新浪科技";
    private static final String NET_TECHNOLOGY = "网易科技";
    private static final String QDAILY = "好奇心日报";
    private static final String LEIPHONE = "雷锋网";
    private static final String HUXIU = "虎嗅";
    private static final String IFANR = "爱范儿";

    public void crawlerInfo() {
        long startTime = System.currentTimeMillis();
        WebClient webClient = initWebClient();

        //百度百家数据抓取
        baiduBaijiaCrawler(webClient);
        //网易科技数据抓取
        netTechnologyCrawler(webClient);
        //好奇心日报数据抓取
        qdailyCrawler(webClient);
        //雷锋网数据抓取
        leiPhoneCrawler(webClient);
        //新浪科技数据抓取
        sinaTechnologyCrawler(webClient);
        //虎嗅数据抓取
        huxiuCrawler(webClient);
        //爱范儿数据抓取
        ifanrCrawler(webClient);

        webClient.closeAllWindows();
        logger.info("crawler all time:" + (System.currentTimeMillis() - startTime));
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        int timeout = webClient.getOptions().getTimeout();
        webClient.getOptions().setTimeout(timeout * 10);
        Calendar calendar = Calendar.getInstance();
        try {
            //String urlString = "http://www.cheekr.com/"; //粹客网
            //String urlString = "http://www.pintu360.com/"; //品途
            //String urlString = "http://www.tmtpost.com/"; //钛媒体 String xPath = "//ul[@class='article-list']/li";
            //String urlString = "http://www.iheima.com/"; //i黑马网 String xPath = "//div[@class='desc']";
            //String urlString = "http://mi.techweb.com.cn/"; //TechWeb String xPath = "//div[@class='con_one bg_grag']";
            //String urlString = "http://www.infoq.com/cn/articles/"; //infoQ文章 String xPath = "//div[@class='news_type1']";
            String urlString = "http://www.ifanr.com/";
            String xPath = "//div[@class='article-item article-item--card ']";
            HtmlPage page = webClient.getPage(urlString);
            List<HtmlDivision> list = (List<HtmlDivision>) page.getByXPath(xPath);
            Iterator<HtmlDivision> ite = list.iterator();
            //System.out.println(list.size());
            while (ite.hasNext()) {
                ArticleInfo articleInfo = new ArticleInfo();
                articleInfo.setSource(IFANR);
                HtmlDivision division = ite.next();
                //System.out.println(picDivisionList.get(0).getAttribute("style"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.closeAllWindows();
    }

    /**
     * 爱范儿数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void ifanrCrawler(WebClient webClient) {

        try {
            Calendar calendar = Calendar.getInstance();
            String urlString = "http://www.ifanr.com/";
            String xPath = "//div[@class='article-item article-item--card ']";
            HtmlPage page = webClient.getPage(urlString);
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
                    }
                    saveArticle(articleInfo);
                } catch (Exception e) {
                    logger.error("ifanrCrawler is exception:" + e.toString());
                }
            }
        } catch (Exception e) {
            logger.error("ifanrCrawler is exception:" + e.toString());
        }
    }

    /**
     * 虎嗅数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void huxiuCrawler(WebClient webClient) {
        try {
            Calendar calendar = Calendar.getInstance();
            String urlString = "http://www.huxiu.com";
            String xPath = "//div[@class='mod-b mod-art ']";
            HtmlPage page = webClient.getPage(urlString);
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
                        articleInfo.setLinkUrl(urlString
                                               + titleAnchorList.get(0).getAttribute("href"));
                    }
                    List<HtmlImage> picImageList = (List<HtmlImage>) division
                        .getByXPath(".//div[@class='mod-thumb']/a/img");
                    if (!CollectionUtils.isEmpty(picImageList)) {
                        articleInfo.setPicUrl(picImageList.get(0).getAttribute("data-original"));
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
    }

    /**
     * 新浪科技数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void sinaTechnologyCrawler(WebClient webClient) {
        try {
            Calendar calendar = Calendar.getInstance();
            String urlString = "http://tech.sina.com.cn/";
            String xPath = "//div[@class='feed-card-item']";
            HtmlPage page = webClient.getPage(urlString);
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
    }

    /**
     * 雷锋网数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void leiPhoneCrawler(WebClient webClient) {
        try {
            String urlString = "http://www.leiphone.com/";
            String xPath = "//div[@class='lph-pageList index-pageList']/div[@class='list']/ul[@class='clr']/li";
            HtmlPage page = webClient.getPage(urlString);
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
    }

    /**
     * 好奇心日报数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void qdailyCrawler(WebClient webClient) {
        try {
            String urlString = "http://www.qdaily.com/tags/29.html";
            String xPath = "//div[@class='packery-item article']/a";
            HtmlPage page = webClient.getPage(urlString);
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
    }

    /**
     * 网易科技数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void netTechnologyCrawler(WebClient webClient) {
        try {
            String urlString = "http://tech.163.com/internet/";
            String xPath = "//ul[@class='newsList']/li";
            HtmlPage page = webClient.getPage(urlString);
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
    }

    /**
     * 百度百家数据抓取
     * 
     */
    @SuppressWarnings("unchecked")
    private void baiduBaijiaCrawler(WebClient webClient) {
        try {
            Calendar calendar = Calendar.getInstance();
            String urlString = "http://baijia.baidu.com/";
            String xPath = "//div[@class='feeds-item hasImg']";
            HtmlPage page = webClient.getPage(urlString);
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
