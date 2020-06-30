package xiaozhuo.info.service.util.crawler;

import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class YiCaiCrawler implements Runnable {

	private static final String YICAI = "第一财经";
	private static final String URL_STRING = "https://www.yicai.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public YiCaiCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
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
					} else {
						articleInfo.setDate(CommonTools.getDateString());
					}
					List<Object> picImageList = (List<Object>) anchor
							.getByXPath(".//div[@class='m-list m-list-1 f-cb']/div[@class='lef f-fl m-zoomin']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("yiCaiCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("yiCaiCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
