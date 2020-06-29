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
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class NetTechnologyCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(NetTechnologyCrawler.class);
	private static final String NET_TECHNOLOGY = "网易科技";
	private static final String URL_STRING = "http://tech.163.com/internet";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public NetTechnologyCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//ul[@class='newsList']/li";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(NET_TECHNOLOGY);
					HtmlListItem division = (HtmlListItem) ite.next();
					List<Object> titleAchorList = (List<Object>) division
							.getByXPath(".//div[@class='titleBar clearfix']/h3[@class='bigsize ']/a");
					if (!CollectionUtils.isEmpty(titleAchorList)) {
						HtmlAnchor anchor = (HtmlAnchor) titleAchorList.get(0);
						articleInfo.setTitle(anchor.asText());
						articleInfo.setLinkUrl(anchor.getAttribute("href"));
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='clearfix']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> descParaList = (List<Object>) division
							.getByXPath(".//div[@class='clearfix']/div[@class='newsDigest']/p");
					if (!CollectionUtils.isEmpty(descParaList)) {
						HtmlParagraph paragraph = (HtmlParagraph) descParaList
								.get(0);
						articleInfo.setIntroduction(paragraph.asText());
					}
					List<Object> authorSpanList = (List<Object>) division
							.getByXPath(".//div[@class='newsBottom clearfix']/p[@class='sourceDate']/span");
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan span = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(span.asText());
					}
					List<Object> timePList = (List<Object>) division
							.getByXPath(".//div[@class='newsBottom clearfix']/p[@class='sourceDate']");
					if (!CollectionUtils.isEmpty(timePList)) {
						HtmlParagraph timeParagraph = (HtmlParagraph) timePList
								.get(0);
						HtmlSpan span1 = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setDate(timeParagraph.asText().replace(
								span1.asText(), ""));
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("netTechnologyCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("netTechnologyCrawler is exception:{}", e.toString());
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
