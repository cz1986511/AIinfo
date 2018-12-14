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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class TechWebCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(TechWebCrawler.class);
	private static final String TECHWEB = "techweb";
	private static final String URL_STRING = "http://mi.techweb.com.cn";
	private static final String DEFAULT_PIC = "http://chenzhuo.pub/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public TechWebCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='con_one bg_grag']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(TECHWEB);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//h2/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='con_img']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//div[@class='con_txt']/p");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> dateSpanList = (List<Object>) division
							.getByXPath(".//div[@class='con_txt']/div[@class='time_tag']/span");
					if (!CollectionUtils.isEmpty(dateSpanList)) {
						HtmlSpan dateSpan = (HtmlSpan) dateSpanList.get(0);
						articleInfo.setDate(dateSpan.asText());
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='con_txt']/div[@class='time_tag']/span[@class='tag']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("techwebCrawler is exception:" + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("techwebCrawler is exception:" + e.toString());
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
