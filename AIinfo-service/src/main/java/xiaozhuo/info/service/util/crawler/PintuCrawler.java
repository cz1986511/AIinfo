package xiaozhuo.info.service.util.crawler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
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
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class PintuCrawler implements Runnable {

	private static final String PINTU = "品途";
	private static final String URL_STRING = "https://www.pintu360.com/";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public PintuCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='article-list']";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(PINTU);
					HtmlDivision division = (HtmlDivision) ite.next();
					List<Object> timeList = division
							.getByXPath(".//div[@class='article-text']/ul[@class='article-data']/li[@class='article-time']");
					if (!CollectionUtils.isEmpty(timeList)) {
						HtmlListItem timeLabel = (HtmlListItem) timeList.get(0);
						Date dateTime = new Date(Long.valueOf(timeLabel
								.getAttribute("createtime")));
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						articleInfo.setDate(sdf.format(dateTime));
					}
					List<Object> authorList = division
							.getByXPath(".//div[@class='article-text']/ul[@class='article-data']/li[@class='article-writer']");
					if (!CollectionUtils.isEmpty(authorList)) {
						HtmlListItem authorItem = (HtmlListItem) authorList
								.get(0);
						articleInfo.setAuthor(authorItem.asText());
					}
					List<Object> tagAnchorList = division
							.getByXPath(".//div[@class='article-text']/ul[@class='article-data']/li[@class='article-lable']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						String tag = "";
						Iterator<Object> tagIterator = tagAnchorList.iterator();
						while (tagIterator.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) tagIterator
									.next();
							tag = tag + tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tag);
					}
					List<Object> titleAnchorList = (List<Object>) division
							.getByXPath(".//div[@class='article-text']/h2[@class='article-title']/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo.setLinkUrl(URL_STRING
								+ titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> descParagraphList = (List<Object>) division
							.getByXPath(".//div[@class='article-text']/p[@class='article-content']");
					if (!CollectionUtils.isEmpty(descParagraphList)) {
						HtmlParagraph descParagraph = (HtmlParagraph) descParagraphList
								.get(0);
						articleInfo.setIntroduction(descParagraph.asText());
					}
					List<Object> picImageList = (List<Object>) division
							.getByXPath(".//div[@class='article-photo']/a/img");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage.getAttribute("src"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("pintuCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("pintuCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
