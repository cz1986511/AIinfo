package xiaozhuo.info.service.util.crawler;

import java.util.Calendar;
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
import xiaozhuo.info.service.util.CommonTools;
import xiaozhuo.info.service.util.Constant;

/**
 * @author Chen
 */
@Slf4j
public class LeiPhoneCrawler implements Runnable {

	private static final String LEIPHONE = "雷锋网";
	private static final String URL_STRING = "http://www.leiphone.com";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public LeiPhoneCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = CommonTools.initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//div[@class='lph-pageList index-pageList']/div[@class='list']/ul[@class='clr']/li";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			Iterator<Object> ite = list.iterator();
			Calendar calendar = Calendar.getInstance();
			while (ite.hasNext()) {
				try {
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setSource(LEIPHONE);
					HtmlListItem item = (HtmlListItem) ite.next();
					List<Object> picImageList = (List<Object>) item
							.getByXPath(".//div[@class='img']/a/img[@class='lazy']");
					if (!CollectionUtils.isEmpty(picImageList)) {
						HtmlImage picImage = (HtmlImage) picImageList.get(0);
						articleInfo.setPicUrl(picImage
								.getAttribute("data-original"));
					} else {
						articleInfo.setPicUrl(Constant.PIC_URL);
					}
					List<Object> titleAnchorList = (List<Object>) item
							.getByXPath(".//div[@class='box']/div[@class='word']/h3/a");
					if (!CollectionUtils.isEmpty(titleAnchorList)) {
						HtmlAnchor titleAnchor = (HtmlAnchor) titleAnchorList
								.get(0);
						articleInfo
								.setLinkUrl(titleAnchor.getAttribute("href"));
						articleInfo.setTitle(titleAnchor.asText());
					}
					List<Object> descDivisionList = (List<Object>) item
							.getByXPath(".//div[@class='box']/div[@class='word']/div[@class='des']");
					if (!CollectionUtils.isEmpty(descDivisionList)) {
						HtmlDivision descDivision = (HtmlDivision) descDivisionList
								.get(0);
						articleInfo.setIntroduction(descDivision.asText());
					}
					List<Object> authorAnchorList = (List<Object>) item
							.getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/a");
					if (!CollectionUtils.isEmpty(authorAnchorList)) {
						HtmlAnchor authAnchor = (HtmlAnchor) authorAnchorList
								.get(0);
						articleInfo.setAuthor(authAnchor.asText());
					}
					List<Object> timeDivisionList = (List<Object>) item
							.getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/div[@class='time']");
					if (!CollectionUtils.isEmpty(timeDivisionList)) {
						HtmlDivision timeDivision = (HtmlDivision) timeDivisionList
								.get(0);
						String time = timeDivision.asText();
						String date = "";
						String[] timeArray = time.split("-");
						if (timeArray.length == 1) {
							String day = (calendar.get(Calendar.MONTH) + 1) > 9 ? ("" + (calendar
									.get(Calendar.MONTH) + 1))
									: ("0" + (calendar.get(Calendar.MONTH) + 1));
							date = "" + calendar.get(Calendar.YEAR) + "-" + day
									+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
						} else {
							date = "" + calendar.get(Calendar.YEAR) + "-"
									+ time;
						}
						articleInfo.setDate(date);
					}
					List<Object> tagAnchorList = (List<Object>) item
							.getByXPath(".//div[@class='box']/div[@class='word']/div[@class='msg clr']/div[@class='tags']/a");
					if (!CollectionUtils.isEmpty(tagAnchorList)) {
						String tag = "";
						Iterator<Object> iterator = tagAnchorList.iterator();
						while (iterator.hasNext()) {
							HtmlAnchor tagAnchor = (HtmlAnchor) iterator.next();
							tag += tagAnchor.asText() + " ";
						}
						articleInfo.setTag(tag);
					}
					articleInfoService.addNewArticle(articleInfo);
				} catch (Exception e) {
					log.error("leiPhoneCrawler is exception:{}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("leiPhoneCrawler is exception:{}", e.toString());
		}
		webClient.close();
	}

}
