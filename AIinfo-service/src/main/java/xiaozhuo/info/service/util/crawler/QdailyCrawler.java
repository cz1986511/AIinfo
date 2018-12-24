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
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class QdailyCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(QdailyCrawler.class);
	private static final String QDAILY = "好奇心日报";
	private static final String URL_STRING = "http://www.qdaily.com/tags/29.html";
	private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public QdailyCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='packery-item article']/a";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(QDAILY);
					HtmlAnchor anchor = (HtmlAnchor) ite.next();
					articleInfo.setLinkUrl("http://www.qdaily.com"
							+ anchor.getAttribute("href"));
					List<Object> picImageList = (List<Object>) anchor
							.getByXPath(".//div[@class='grid-article-hd']/div[@class='pic imgcover']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo
								.setPicUrl(picImage.getAttribute("data-src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> h3TitleList = (List<Object>) anchor
							.getByXPath(".//div[@class='grid-article-bd']/h3");
					if (!CollectionUtils.isEmpty(h3TitleList)) {
						HtmlHeading3 heading3 = (HtmlHeading3) h3TitleList
								.get(0);
						articleInfo.setTitle(heading3.asText());
					}
					List<Object> timeSpanList = (List<Object>) anchor
							.getByXPath(".//div[@class='grid-article-ft clearfix']/span");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						articleInfo.setDate(timeSpan.getAttribute(
								"data-origindate").substring(0, 19));
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("qdailyCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("qdailyCrawler is exception:" + e.toString());
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
