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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSection;

public class ZhiHuCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ZhiHuCrawler.class);
	private static final String ZHIHU = "知乎";
	private static final String URL_STRING = "https://www.zhihu.com/hot";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public ZhiHuCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//section[@class='HotItem']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(ZHIHU);
					HtmlSection section = (HtmlSection) ite.next();
					List<Object> titleAnchorList = (List<Object>) section
							.getByXPath(".//div[@class='HotItem-content']/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.getAttribute("title"));
					}
					List<Object> descParagraphList = (List<Object>) section
							.getByXPath(".//div[@class='HotItem-content']/a/p[@class='HotItem-excerpt']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}

					List<Object> picImageList = (List<Object>) section
							.getByXPath(".//a[@class='HotItem-img']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("zhiHuCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("zhiHuCrawler is exception:" + e.toString());
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
		int count = 0;
		try {
			String xPath = "//section[@class='HotItem']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(ZHIHU);
					HtmlSection section = (HtmlSection) ite.next();
					List<Object> titleAnchorList = (List<Object>) section
							.getByXPath(".//div[@class='HotItem-content']/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.getAttribute("title"));
					}
					List<Object> descParagraphList = (List<Object>) section
							.getByXPath(".//div[@class='HotItem-content']/a/p[@class='HotItem-excerpt']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}

					List<Object> picImageList = (List<Object>) section
							.getByXPath(".//a[@class='HotItem-img']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					count++;
				} catch (Exception e) {
					logger.error("zhiHuCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("zhiHuCrawler is exception:" + e.toString());
		}
		webClient.close();
		System.out.println(count);
	}

}
