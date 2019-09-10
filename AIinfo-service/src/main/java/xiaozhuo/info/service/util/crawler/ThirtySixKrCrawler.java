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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class ThirtySixKrCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ThirtySixKrCrawler.class);
	private static final String TSKR = "36氪";
	private static final String URL_STRING = "https://www.36kr.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public ThirtySixKrCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='kr-home-flow-item']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(TSKR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-info clearfloat']/p[@class='title-wrapper ellipsis-2']/a[@class='article-item-title weight-bold']");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-info clearfloat']/div[@class='kr-flow-bar']/span[@class='kr-flow-bar-motif']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
					}
					List<Object> authorAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-info clearfloat']/div[@class='kr-flow-bar']/a[@class='kr-flow-bar-author']");
					if (!CollectionUtils.isEmpty(authorAnchorList)) {
						HtmlAnchor authorAnchor = (HtmlAnchor) authorAnchorList
								.get(0);
						articleInfo.setAuthor(authorAnchor.asText());
					}
					List<Object> timeSpanList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-info clearfloat']/div[@class='kr-flow-bar']/span[@class='kr-flow-bar-time']");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						String timeString = timeSpan.asText();
						Long tLong = 0L;
						Date dateTime = new Date();
						if (timeString.contains("分钟前")) {
							String[] timeArray = timeString.split("分");
							int para = Integer.valueOf(timeArray[0]);
							tLong = dateTime.getTime() - (para * 60000);
						} else if (timeString.contains("小时前")) {
							String[] timeArray = timeString.split("小");
							int para = Integer.valueOf(timeArray[0]);
							tLong = dateTime.getTime() - (para * 60 * 60000);
						}
						if (tLong > 0L) {
							Date ndateTime = new Date(tLong);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							articleInfo.setDate(sdf.format(ndateTime));
						} else {
							articleInfo.setDate(timeString);
						}
					}
					List<Object> descDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-info clearfloat']/a[@class='article-item-description ellipsis-2']");
					if (!CollectionUtils.isEmpty(descDivisionList)) {
						HtmlAnchor descAnchor = (HtmlAnchor) descDivisionList
								.get(0);
						articleInfo.setIntroduction(descAnchor.asText());
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='kr-shadow-wrapper']/div[@class='kr-shadow-content']/div[@class='article-item-pic-wrapper']/a[@class='article-item-pic']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("thirtySixKrCrawler is exception:"
							+ e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("thirtySixKrCrawler is exception:" + e.toString());
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
