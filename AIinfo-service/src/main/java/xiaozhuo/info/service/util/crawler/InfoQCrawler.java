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
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

public class InfoQCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(InfoQCrawler.class);
	private static final String INFOQ = "InfoQ";
	private static final String URL_STRING = "https://www.infoq.cn/";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public InfoQCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='list-item image-position-right']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(INFOQ);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> timeDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='info']/div[@class='extra']/div[@class='date']");
					if (!CollectionUtils.isEmpty(timeDivisionList)) {
						HtmlDivision timeDivision = (HtmlDivision) timeDivisionList
								.get(0);
						Date dateTime = new Date();
						Long tLong = 0L;
						String timeString = timeDivision.asText();
						String[] arrayTimes = timeString.split(" ");
						int para = Integer.valueOf(arrayTimes[0]);
						if (timeString.contains("分钟前")) {
							tLong = dateTime.getTime() - para * 60000;
						}
						if (timeString.contains("小时前")) {
							tLong = dateTime.getTime() - para * 60 * 60000;
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
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='info']/h6[@class='favorite with-image']/a[@class='com-article-title']");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//div[@class='info']/p[@class='summary']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> authorAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='info']/p[@class='editor with-image']/a[@class='com-author-name author']");
					if (!CollectionUtils.isEmpty(authorAnchorList)) {
						HtmlAnchor authAnchor = (HtmlAnchor) authorAnchorList
								.get(0);
						articleInfo.setAuthor(authAnchor.asText());
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='info']/div[@class='extra']/div[@class='com-topic-tag topic']/a[@class='com-topic-title topic-tag-title']");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						String tagString = "";
						Iterator<Object> tagIterator = tagAnchorList.iterator();
						while (tagIterator.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) tagIterator
									.next();
							tagString = tagString + tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tagString);
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='image']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("infoQCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("infoQCrawler is exception:" + e.toString());
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
