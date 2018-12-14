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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;

public class IfanrCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(IfanrCrawler.class);
	private static final String IFANR = "爱范儿";
	private static final String URL_STRING = "http://www.ifanr.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public IfanrCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			Calendar calendar = Calendar.getInstance();
			String xPath = "//div[@class='article-item article-item--card ']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(IFANR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//a[@class='article-label']");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
					}
					List<Object> urlAnchorList = (List<Object>) division
							.getByXPath(".//a[@class!='article-label']");
					if (!CollectionUtils.isEmpty(urlAnchorList)) {
						HtmlAnchor urlAnchor = (HtmlAnchor) urlAnchorList
								.get(0);
						articleInfo.setLinkUrl(urlAnchor.getAttribute("href"));
					}
					List<Object> titleH3List = (List<Object>) division
							.getByXPath(".//h3");
					if (!CollectionUtils.isEmpty(titleH3List)) {
						HtmlHeading3 titleHeading3 = (HtmlHeading3) titleH3List
								.get(0);
						articleInfo.setTitle(titleHeading3.asText());
					}
					List<Object> timeSpanList = (List<Object>) division
							.getByXPath(".//time");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlUnknownElement timeSpan = (HtmlUnknownElement) timeSpanList
								.get(0);
						String time = timeSpan.asText();
						String date = "";
						String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
								.get(Calendar.MONTH) + 1)) : ("0" + (calendar
								.get(Calendar.MONTH) + 1));
						if (time.contains("昨天")) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 1);
						} else if (time.contains("前天")) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 2);
						} else {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
						}
						articleInfo.setDate(date);
					}
					List<Object> picDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='article-image cover-image']");
					if (!CollectionUtils.isEmpty(picDivisionList)) {
						HtmlDivision picDivision = (HtmlDivision) picDivisionList
								.get(0);
						String picUrls = picDivision.getAttribute("style");
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
