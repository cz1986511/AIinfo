package xiaozhuo.info.service.util.crawler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class HuxiuCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(HuxiuCrawler.class);
	private static final String HUXIU = "虎嗅";
	private static final String URL_STRING = "https://www.huxiu.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public HuxiuCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			Calendar calendar = Calendar.getInstance();
			String xPath = "//div[@class='mod-b mod-art clearfix ']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(HUXIU);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='column-link-box']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						Iterator<Object> iteTag = tagAnchorList.iterator();
						String tag = "";
						while (iteTag.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) iteTag.next();
							tag = tag + tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tag);
					}
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/h2/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='mod-thumb pull-left ']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage
								.getAttribute("data-original"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> authorSpanList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-author']/a/span[@class='author-name ']");
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> timeSpanList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-author']/span[@class='time']");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						String time = timeSpan.asText();
						String date = "";
						String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
								.get(Calendar.MONTH) + 1)) : ("0" + (calendar
								.get(Calendar.MONTH) + 1));
						if ("1天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 1);
						} else if ("2天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 2);
						} else {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
						}
						articleInfo.setDate(date);
					}
					List<Object> descDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-sub']");
					if (!CollectionUtils.isEmpty(descDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("huxiuCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("huxiuCrawler is exception:" + e.toString());
		}
		webClient.close();
	}

	private void saveArticle(ArticleInfo articleInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", articleInfo.getTitle());
		map.put("source", articleInfo.getSource());
		map.put("offset", 0);
		map.put("limit", 200);
		List<ArticleInfo> result = articleInfoManager
				.getArticleInfosByParams(map);
		if (CollectionUtils.isEmpty(result)) {
			articleInfoManager.addArticleInfo(articleInfo);
		}
	}

	private WebClient initWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
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
	
	public static void main(String[] args) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		int timeout = webClient.getOptions().getTimeout();
		webClient.getOptions().setTimeout(timeout * 10);
		try {
			Calendar calendar = Calendar.getInstance();
			String xPath = "//div[@class='mod-b mod-art clearfix ']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(HUXIU);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='column-link-box']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						Iterator<Object> iteTag = tagAnchorList.iterator();
						String tag = "";
						while (iteTag.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) iteTag.next();
							tag = tag + tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tag);
					}
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/h2/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='mod-thumb pull-left ']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage
								.getAttribute("data-original"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> authorSpanList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-author']/a/span[@class='author-name ']");
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> timeSpanList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-author']/span[@class='time']");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						String time = timeSpan.asText();
						String date = "";
						String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
								.get(Calendar.MONTH) + 1)) : ("0" + (calendar
								.get(Calendar.MONTH) + 1));
						if ("1天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 1);
						} else if ("2天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 2);
						} else {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
						}
						articleInfo.setDate(date);
					}
					List<Object> descDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='mob-ctt index-article-list-yh']/div[@class='mob-sub']");
					if (!CollectionUtils.isEmpty(descDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					System.out.println(articleInfo.getTitle());
				} catch (Exception e) {
					logger.error("huxiuCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("huxiuCrawler is exception:" + e.toString());
		}
		webClient.close();
	}

}
