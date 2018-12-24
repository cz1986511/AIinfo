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
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class CheekerCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(CheekerCrawler.class);
	private static final String CHEEKR = "粹客网";
	private static final String URL_STRING = "http://www.cheekr.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public CheekerCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='media']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(CHEEKR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='media_text']/h2[@class='indexmediatitle']/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//div[@class='media_text']/span[@class='abstract']/p");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> authorAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='media_text']/div[@class='author_and_time']/span[@class='author']/a");
					if (!CollectionUtils.isEmpty(authorAnchorList)) {
						HtmlAnchor authorAnchor = (HtmlAnchor) authorAnchorList
								.get(0);
						articleInfo.setAuthor(authorAnchor.asText());
					}
					List<Object> tagSpanList = (List<Object>) division
							.getByXPath(".//div[@class='media_text']/span[@class='tags']");
					if (!CollectionUtils.isEmpty(tagSpanList)) {
						String tags = "";
						Iterator<Object> tagIterator = tagSpanList.iterator();
						while (tagIterator.hasNext()) {
							HtmlSpan span = (HtmlSpan) tagIterator.next();
							tags += span.asText() + " ";
						}
						articleInfo.setTag(tags);
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//a[@class='mediaimg_a']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("cheekerCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("cheekerCrawler is exception:" + e.toString());
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
