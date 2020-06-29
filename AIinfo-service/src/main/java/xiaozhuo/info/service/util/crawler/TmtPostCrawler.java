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
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class TmtPostCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(TmtPostCrawler.class);
	private static final String TMTPOST = "钛媒体";
	private static final String URL_STRING = "http://www.tmtpost.com";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoManager;

	public TmtPostCrawler(ArticleInfoService articleInfoManager) {
		super();
		this.webClient = initWebClient();
		this.articleInfoManager = articleInfoManager;
	}

	@Override
	public void run() {
		try {
			String xPath = "//li[@class='post_part clear']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(TMTPOST);
					HtmlListItem listItem = (HtmlListItem) ite.next();
					List<Object> picImageList = (List<Object>) listItem
							.getByXPath(".//a[@class='pic']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage
								.getAttribute("data-original"));
					} else {
						articleInfo.setPicUrl(DEFAULT_PIC);
					}
					List<Object> titleAnchorList = (List<Object>) listItem
							.getByXPath(".//div[@class='cont']/h3/a[@class='title']");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> dateSpanList = (List<Object>) listItem
							.getByXPath(".//div[@class='cont']/div[@class='info']/span[@class='author']");
					if (!CollectionUtils.isEmpty(dateSpanList)) {
						HtmlSpan dateSpan = (HtmlSpan) dateSpanList.get(0);
						String dataString = dateSpan.asText();
						String[] aStrings = dataString.split("•");
						articleInfo.setAuthor(aStrings[0]);
						articleInfo.setDate(aStrings[1]);
					}
					List<Object> descParagraphList = (List<Object>) listItem
							.getByXPath(".//div[@class='cont']/div[@class='info']/p[@class='summary']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> tagDivisionList = (List<Object>) listItem
							.getByXPath(".//div[@class='cont']/div[@class='tag']");
					if (!CollectionUtils.isEmpty(tagDivisionList)) {
						HtmlDivision tagDivision = (HtmlDivision) tagDivisionList
								.get(0);
						articleInfo.setTag(tagDivision.asText().replace("，",
								" "));
					}
					saveArticle(articleInfo);
				} catch (Exception e) {
					logger.error("tmtPostCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("tmtPostCrawler is exception:{}", e.toString());
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
