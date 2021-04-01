package xiaozhuo.info.service.util.crawler;

import java.util.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading5;
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
public class HuxiuCrawler implements Runnable {

	private static final String HUXIU = "虎嗅";
	private static final String URL_STRING = "https://www.huxiu.com/article/";
	private static final String ART_URL = "https://www.huxiu.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public HuxiuCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			Calendar calendar = Calendar.getInstance();
			String xPath = "//div[@class='article-items']";
			HtmlPage page = webClient.getPage(URL_STRING);
			webClient.waitForBackgroundJavaScript(5000);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(HUXIU);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> linkAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-item  article-item--normal']/a");
					if (!CollectionUtils.isEmpty(linkAnchorList)) {
						HtmlAnchor linkAnchor = (HtmlAnchor) linkAnchorList.get(0);
						articleInfo.setLinkUrl(ART_URL + (linkAnchor.getAttribute("href")));
					}
					List<Object> titleAnchorList = (List<Object>) division.getByXPath(
							".//div[@class='article-item  article-item--normal']/a/div[@class='article-item__content']/h5");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlHeading5 titleH5 = (HtmlHeading5) titleAnchorList.get(0);
						articleInfo.setTitle(titleH5.asText());
					}
					articleInfo.setPicUrl(Constant.PIC_URL);
					List<Object> authorSpanList = (List<Object>) division.getByXPath(
							".//div[@class='article-item  article-item--normal']/div[@class='article-item__content__user-info ']/a/span");
					if (!CollectionUtils.isEmpty(authorSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorSpanList.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> timeSpanList = (List<Object>) division.getByXPath(
							".//div[@class='article-item  article-item--normal']/a/div[@class='article-item__content']/div[@class='article-item__content__status']/span[@class='article-item__content__status__date']");
					if (!CollectionUtils.isEmpty(timeSpanList)) {
						HtmlSpan timeSpan = (HtmlSpan) timeSpanList.get(0);
						String time = timeSpan.asText();
						String date = "";
						String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar.get(Calendar.MONTH) + 1))
								: ("0" + (calendar.get(Calendar.MONTH) + 1));
						if ("1天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 1);
						} else if ("2天前".equals(time)) {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
									+ (calendar.get(Calendar.DAY_OF_MONTH) - 2);
						} else {
							date = "" + calendar.get(Calendar.YEAR) + "-" + day + "-"
									+ calendar.get(Calendar.DAY_OF_MONTH);
						}
						articleInfo.setDate(date);
					} else {
						articleInfo.setDate(CommonTools.getDateString());
					}
					List<Object> descHtmlParagraphList = (List<Object>) division.getByXPath(
							".//div[@class='article-item  article-item--normal']/div[@class='article-item__content']/a/p");
					if (!CollectionUtils.isEmpty(descHtmlParagraphList)) {
						HtmlParagraph descdescHtmlParagraphList = (HtmlParagraph) descHtmlParagraphList.get(0);
						articleInfo.setIntroduction(descdescHtmlParagraphList.asText());
					}
					if(null != articleInfo.getSource() && null != articleInfo.getTitle()) {
						articleInfoService.addNewArticle(articleInfo);
					}
				} catch (Exception e) {
					log.error("huxiuCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("huxiuCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
