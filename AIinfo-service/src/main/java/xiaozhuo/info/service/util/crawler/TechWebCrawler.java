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
import com.gargoylesoftware.htmlunit.html.HtmlHeading4;
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
public class TechWebCrawler implements Runnable {

	private static final String TECHWEB = "移动互联";
	private static final String URL_STRING = "http://www.techweb.com.cn/mobile";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public TechWebCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='picture_text']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(TECHWEB);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> titleHeading4List = (List<Object>) division
							.getByXPath(".//div[@class='text']/a/h4");
					if (!CollectionUtils.isEmpty(titleHeading4List)) {
						HtmlHeading4 titleHeading4 = (HtmlHeading4) titleHeading4List
								.get(0);
						articleInfo.setTitle(titleHeading4.asText());

					}
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='text']/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='text']/div[@class='time_tag']/span[@class='tags']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						Iterator<Object> tagIterator = tagAnchorList.iterator();
						String tag = "";
						while (tagIterator.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) tagIterator
									.next();
							tag = tag + tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tag);
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='picture']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//div[@class='text']/p");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> dateSpanList = (List<Object>) division
							.getByXPath(".//div[@class='text']/div[@class='time_tag']/span");
					if (!CollectionUtils.isEmpty(dateSpanList)) {
						HtmlSpan dateSpan = (HtmlSpan) dateSpanList.get(0);
						articleInfo.setDate(dateSpan.asText());
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("techwebCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("techwebCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
