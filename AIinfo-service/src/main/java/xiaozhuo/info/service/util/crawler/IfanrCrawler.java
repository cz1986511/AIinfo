package xiaozhuo.info.service.util.crawler;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTime;

public class IfanrCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(IfanrCrawler.class);
	private static final String IFANR = "爱范儿";
	private static final String URL_STRING = "https://www.ifanr.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

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
			String xPath = "//div[@class='article-item article-item--list ']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(IFANR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> authorHtmlSpanList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-meta']/div[@class='author-info']/span");
					if (!CollectionUtils.isEmpty(authorHtmlSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorHtmlSpanList
								.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> titleHtmlAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/h3/a");
					if (!CollectionUtils.isEmpty(titleHtmlAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleHtmlAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
					}
					List<Object> descHtmlDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-summary']");
					if (!CollectionUtils.isEmpty(descHtmlDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descHtmlDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					List<Object> timeList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-meta']/time");
					if (!CollectionUtils.isEmpty(timeList)) {
						HtmlTime timeSpan = (HtmlTime) timeList.get(0);
						String time = timeSpan.getAttribute("data-timestamp");
						Date nDate = new Date(Long.valueOf(time) * 1000L);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						articleInfo.setDate(sdf.format(nDate));
					}
					List<Object> picAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-image cover-image']/a[@class='article-link cover-block']");
					if (!CollectionUtils.isEmpty(picAnchorList)) {
						HtmlAnchor picAnchor = (HtmlAnchor) picAnchorList
								.get(0);
						String picUrls = picAnchor.getAttribute("style");
						String[] picUrlArray = picUrls.split("'");
						articleInfo.setPicUrl(picUrlArray[1]);
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-image cover-image']/a[@class='article-label']");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
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

}
