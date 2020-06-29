package xiaozhuo.info.service.util.crawler;

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
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class YiCaiCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(YiCaiCrawler.class);
	private static final String YICAI = "第一财经";
	private static final String URL_STRING = "https://www.yicai.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public YiCaiCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//a[@class='f-db']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(YICAI);
					HtmlAnchor anchor = (HtmlAnchor) ite.next();
					String linkString = anchor.getAttribute("href");
					if (linkString.contains("video")) {
						continue;
					} else {
						articleInfo.setLinkUrl(URL_STRING + linkString);
					}
					List<Object> titletitleHeading2List = (List<Object>) anchor
							.getByXPath(".//div[@class='m-list m-list-1 f-cb']/div/h2");
					if (!CollectionUtils.isEmpty(titletitleHeading2List)) {
						HtmlHeading2 titleHeading2 = (HtmlHeading2) titletitleHeading2List
								.get(0);
						articleInfo.setTitle(titleHeading2.asText());
					}
					List<Object> descParagraphList = (List<Object>) anchor
							.getByXPath(".//div[@class='m-list m-list-1 f-cb']/div/p");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> timeSpanList = (List<Object>) anchor
							.getByXPath(".//div[@class='m-list m-list-1 f-cb']/div/div[@class='author']/span");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						articleInfo.setDate(timeSpan.asText());
					}
					List<Object> picImageList = (List<Object>) anchor
							.getByXPath(".//div[@class='m-list m-list-1 f-cb']/div[@class='lef f-fl m-zoomin']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("yiCaiCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("yiCaiCrawler is exception:{}", e.toString());
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
