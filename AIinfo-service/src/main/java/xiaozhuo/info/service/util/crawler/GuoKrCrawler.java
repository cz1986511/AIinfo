package xiaozhuo.info.service.util.crawler;

import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class GuoKrCrawler implements Runnable {

	private static final String GUOKR = "果壳网";
	private static final String URL_STRING = "https://www.guokr.com/scientific/";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public GuoKrCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='article']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(GUOKR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//h3/a[@class='article-title']");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//p[@class='article-summary']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> authorSpanList = (List<Object>) division
							.getByXPath(".//div[@class='article-info']/span[@class='article-author-os']");
					if (CollectionUtils.isEmpty(authorSpanList)) {
						authorSpanList = (List<Object>) division
								.getByXPath(".//div[@class='article-info']/span[@class='article-author']");
					}
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//a[@class='label label-common']");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("guoKrCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("guoKrCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}
	
}
