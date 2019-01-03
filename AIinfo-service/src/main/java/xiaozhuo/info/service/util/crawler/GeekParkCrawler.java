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
import com.gargoylesoftware.htmlunit.html.HtmlArticle;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

public class GeekParkCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(GeekParkCrawler.class);
	private static final String GEEKPARK = "极客公园";
	private static final String URL_STRING = "http://www.geekpark.net/";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public GeekParkCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//article[@class='article-item']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(GEEKPARK);
					HtmlArticle listItem = (HtmlArticle) ite.next();
					List<Object> titleHeading3List = (List<Object>) listItem
							.getByXPath(".//div[@class='article-info']/a/h3[@class='multiline-text-overflow']");
					if (!CollectionUtils.isEmpty(titleHeading3List)) {
						HtmlHeading3 titleHeading3 = (HtmlHeading3) titleHeading3List
								.get(0);
						articleInfo.setTitle(titleHeading3.asText());
					}
					List<Object> linkAnchorList = (List<Object>) listItem
							.getByXPath(".//a[@class='img-cover-wrap']");
					if (!CollectionUtils.isEmpty(linkAnchorList)) {
						HtmlAnchor linkAnchor = (HtmlAnchor) linkAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ linkAnchor.getAttribute("href"));
					}
					List<Object> descParagraphList = (List<Object>) listItem
							.getByXPath(".//div[@class='article-info']/p[@class='multiline-text-overflow']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> dateDivisionList = (List<Object>) listItem
							.getByXPath(".//div[@class='article-info']/div[@class='article-time']");
					if (!CollectionUtils.isEmpty(dateDivisionList)) {
						HtmlDivision dateSpan = (HtmlDivision) dateDivisionList
								.get(0);
						String timeString = dateSpan.asText();
						String[] timeArray = timeString.split(" ");
						if (timeArray.length > 1) {
							Long tLong = 0L;
							int param = Integer.valueOf(timeArray[0]);
							Date dateTime = new Date();
							tLong = dateTime.getTime() - param * 60 * 60000;
							Date ndateTime = new Date(tLong);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							articleInfo.setDate(sdf.format(ndateTime));
						} else {
							articleInfo.setDate(timeString);
						}
					}
					List<Object> authorAnchorList = (List<Object>) listItem
							.getByXPath(".//div[@class='article-meta hidden-xs']/a[@class='article-author']");
					if (!CollectionUtils.isEmpty(authorAnchorList)) {
						HtmlAnchor authorAnchor = (HtmlAnchor) authorAnchorList
								.get(0);
						articleInfo.setAuthor(authorAnchor.asText());
					}
					List<Object> picImageList = (List<Object>) listItem
							.getByXPath(".//a[@class='img-cover-wrap']/div[@class='img-cover']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("GeekParkCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("GeekParkCrawler is exception:" + e.toString());
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
