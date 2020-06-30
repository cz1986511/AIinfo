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
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class TmtPostCrawler implements Runnable {

	private static final String TMTPOST = "钛媒体";
	private static final String URL_STRING = "http://www.tmtpost.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public TmtPostCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
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
						articleInfo.setPicUrl(Constant.PIC_URL);
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
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("tmtPostCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("tmtPostCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
