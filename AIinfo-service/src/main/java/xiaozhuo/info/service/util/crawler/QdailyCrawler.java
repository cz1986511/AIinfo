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
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
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
public class QdailyCrawler implements Runnable {

	private static final String QDAILY = "好奇心日报";
	private static final String URL_STRING = "http://www.qdaily.com/tags/29.html";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public QdailyCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='packery-item article animated fadeIn']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(QDAILY);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = division.getByXPath(".//a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl("http://www.qdaily.com"
								+ titleAnchor.getAttribute("href"));
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//a/div[@class='grid-article-hd']/div[@class='pic imgcover']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo
								.setPicUrl(picImage.getAttribute("data-src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					List<Object> h3TitleList = (List<Object>) division
							.getByXPath(".//a/div[@class='grid-article-bd']/h3");
					if (!CollectionUtils.isEmpty(h3TitleList)) {
						HtmlHeading3 heading3 = (HtmlHeading3) h3TitleList
								.get(0);
						articleInfo.setTitle(heading3.asText());
					}
					List<Object> tagParagraphList = (List<Object>) division
							.getByXPath(".//a[@class='com-grid-article']/div[@class='grid-article-hd']/p[@class='category']");
					if (!CollectionUtils.isEmpty(tagParagraphList)) {
						HtmlParagraph tagParagraph = (HtmlParagraph) tagParagraphList
								.get(0);
						articleInfo.setTag(tagParagraph.asText());
					}
					List<Object> timeSpanList = (List<Object>) division
							.getByXPath(".//a/div[@class='grid-article-ft clearfix']/span");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						articleInfo.setDate(timeSpan.getAttribute(
								"data-origindate").substring(0, 19));
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("qdailyCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("qdailyCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
