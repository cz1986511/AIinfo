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
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class IheimaCrawler implements Runnable {

	private static final String IHEIMA = "i黑马";
	private static final String URL_STRING = "http://www.iheima.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public IheimaCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='item-wrap clearfix']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(IHEIMA);
					HtmlDivision listItem = (HtmlDivision) ite.next();
					List<Object> titleAnchorList = (List<Object>) listItem
							.getByXPath(".//div[@class='desc distable-cell']/a[@class='title']");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
					}
					List<Object> descDivisionList = (List<Object>) listItem
							.getByXPath(".//div[@class='desc distable-cell']/div[@class='brief']");
					if (!CollectionUtils.isEmpty(descDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					List<Object> dateSpanList = (List<Object>) listItem
							.getByXPath(".//div[@class='desc distable-cell']/div[@class='author']/span[@class='timewarp']/span[@class='time']");
					if (!CollectionUtils.isEmpty(dateSpanList)) {
						HtmlSpan dateSpan = (HtmlSpan) dateSpanList.get(0);
						articleInfo.setDate(dateSpan.asText());
					}
					List<Object> authorSpanList = (List<Object>) listItem
							.getByXPath(".//div[@class='desc distable-cell']/div[@class='author']/a/span[@class='name']");
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> picImageList = (List<Object>) listItem
							.getByXPath(".//a[@class='pic distable-cell']/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("iheimaCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("iheimaCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
