package xiaozhuo.info.service.util.crawler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;

public class DoubanCrawler implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(DoubanCrawler.class);
	private static final String DOUBAN = "豆瓣";
	private static final String URL_STRING = "https://movie.douban.com/coming";
	private static final String DEFAULT_PIC = "http://chenzhuo.info/default.png";

	private WebClient webClient;
	private ArticleInfoService articleInfoService;

	public DoubanCrawler(ArticleInfoService articleInfoService) {
		super();
		this.webClient = initWebClient();
		this.articleInfoService = articleInfoService;
	}

	@Override
	public void run() {
		try {
			String xPath = "//table[@class='coming_list']/tbody/tr";
			HtmlPage page = webClient.getPage(URL_STRING);
			List<Object> list = (List<Object>) page.getByXPath(xPath);
			if (null != list) {
				list.forEach(obj ->{
					HtmlTableRow row = (HtmlTableRow) obj;
					HtmlTableCell cell = row.getCells().get(1);
					HtmlAnchor anchor = (HtmlAnchor) cell.getChildNodes().get(1);
					System.out.println(row.getCells().get(0).asText()+ anchor.getHrefAttribute() + anchor.asText() + 
							row.getCells().get(2).asText() + row.getCells().get(3).asText() + row.getCells().get(4).asText());
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		webClient.close();
	}

	private void saveArticle(ArticleInfo articleInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", articleInfo.getTitle());
		map.put("source", articleInfo.getSource());
		map.put("offset", 0);
		map.put("limit", 200);
		List<ArticleInfo> result = articleInfoService
				.getArticleInfosByParams(map);
		if (CollectionUtils.isEmpty(result)) {
			articleInfoService.addArticleInfo(articleInfo);
		}
	}

	private WebClient initWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		int timeout = webClient.getOptions().getTimeout();
		webClient.getOptions().setTimeout(timeout * 10);
		return webClient;
	}
	
	public static void main(String[] args) {
		String s = "abcdefghijklmnopqrstuvwxyz";
		
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 5000000; i++) {
			reverse1(s);
		}
		long end1 = System.currentTimeMillis();
		System.out.println("递归:" + (end1 - start1));
		
		long start2 = System.currentTimeMillis();
		for (int i = 0; i < 5000000; i++) {
			reverse2(s);
		}
		long end2 = System.currentTimeMillis();
		System.out.println("倒序拼接:" + (end2 - start2));
		
		long start3 = System.currentTimeMillis();
		for (int i = 0; i < 5000000; i++) {
			reverse3(s);
		}
		long end3 = System.currentTimeMillis();
		System.out.println("冒泡对调:" + (end3 - start3));
		
		long start4 = System.currentTimeMillis();
		for (int i = 0; i < 5000000; i++) {
			reverse4(s);
		}
		long end4 = System.currentTimeMillis();
		System.out.println("StringBuffer.reverse():" + (end4 - start4));
		
		
	}
	
	//递归
	private static String reverse1(String s) {
		int length = s.length();
		if (length <= 1) {
			return s;
		}
		String left = s.substring(0, length / 2);
		String right = s.substring(length / 2, length);
		return reverse1(right) + reverse1(left);
	}
	//倒序拼接
	private static String reverse2(String s) {
		int length = s.length();
		char[] array = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i = length - 1; i >= 0; i--) {
			sb.append(array[i]);
		}
		return sb.toString();
	}
	//冒泡对调
	private static String reverse3(String s) {
		int length = s.length();
		int n = length -1;
		char[] array = s.toCharArray();
		int half = length / 2;
		for(int i = 0 ; i <= half; i++) {
			char temp = array[i];
			array[i] = array[n - i];
			array[n - i] = temp;
		}
		return new String(array);
	}
	//StringBuffer.reverse()
	private static String reverse4(String s) {
		return new StringBuffer(s).reverse().toString();
	}

}
