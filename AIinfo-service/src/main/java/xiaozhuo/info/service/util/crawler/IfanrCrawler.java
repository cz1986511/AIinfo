package xiaozhuo.info.service.util.crawler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTime;
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class IfanrCrawler implements Runnable {

	private static final String IFANR = "爱范儿";
	private static final String URL_STRING = "https://www.ifanr.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public IfanrCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='article-item article-item--list ']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(IFANR);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> authorHtmlSpanList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-meta']/div[@class='author-info']/span");
					if (!CollectionUtils.isEmpty(authorHtmlSpanList)) {
						HtmlSpan authorSpan = (HtmlSpan) authorHtmlSpanList
								.get(0);
						articleInfo.setAuthor(authorSpan.asText());
					}
					List<Object> titleHtmlAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/h3/a");
					if (!CollectionUtils.isEmpty(titleHtmlAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleHtmlAnchorList
								.get(0);
						articleInfo.setTitle(titleAnchor.asText());
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
					}
					List<Object> descHtmlDivisionList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-summary']");
					if (!CollectionUtils.isEmpty(descHtmlDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descHtmlDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					List<Object> timeList = (List<Object>) division
							.getByXPath(".//div[@class='article-info js-transform']/div[@class='article-meta']/time");
					if (!CollectionUtils.isEmpty(timeList)) {
						HtmlTime timeSpan = (HtmlTime) timeList.get(0);
						String time = timeSpan.getAttribute("data-timestamp");
						Date nDate = new Date(Long.valueOf(time) * 1000L);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						articleInfo.setDate(sdf.format(nDate));
					}
					List<Object> picAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-image cover-image']/a[@class='article-link cover-block']");
					if (!CollectionUtils.isEmpty(picAnchorList)) {
						HtmlAnchor picAnchor = (HtmlAnchor) picAnchorList
								.get(0);
						String picUrls = picAnchor.getAttribute("style");
						String[] picUrlArray = picUrls.split("'");
						articleInfo.setPicUrl(picUrlArray[1]);
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					List<Object> tagAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-image cover-image']/a[@class='article-label']");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						HtmlAnchor tagAnchor = (HtmlAnchor) tagAnchorList
								.get(0);
						articleInfo.setTag(tagAnchor.asText());
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("ifanrCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("ifanrCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
