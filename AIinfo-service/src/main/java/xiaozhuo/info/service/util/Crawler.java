package xiaozhuo.info.service.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import xiaozhuo.info.service.ArticleInfoService;
import xiaozhuo.info.service.util.crawler.BaiduBaijiaCrawler;
import xiaozhuo.info.service.util.crawler.CheekerCrawler;
import xiaozhuo.info.service.util.crawler.HuxiuCrawler;
import xiaozhuo.info.service.util.crawler.IfanrCrawler;
import xiaozhuo.info.service.util.crawler.IheimaCrawler;
import xiaozhuo.info.service.util.crawler.LeiPhoneCrawler;
import xiaozhuo.info.service.util.crawler.NetTechnologyCrawler;
import xiaozhuo.info.service.util.crawler.PintuCrawler;
import xiaozhuo.info.service.util.crawler.QdailyCrawler;
import xiaozhuo.info.service.util.crawler.SinaTechnologyCrawler;
import xiaozhuo.info.service.util.crawler.TechWebCrawler;
import xiaozhuo.info.service.util.crawler.TmtPostCrawler;

@Component
@Configurable
@EnableScheduling
@Service
public class Crawler {

	private static Logger logger = LoggerFactory.getLogger(Crawler.class);

	@Autowired
	private ArticleInfoService articleInfoService;

	private ThreadPoolExecutor executor;

	public void init() {
		this.executor = new ThreadPoolExecutor(2, 5, 2000,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	@Scheduled(cron = "0 7 0/2 * * ?")
	public void crawlerInfo() {
		try {
			if (null == executor) {
				init();
			}
			// i黑马数据抓取
			IheimaCrawler iheimaCrawler = new IheimaCrawler(articleInfoService);
			executor.execute(iheimaCrawler);
			// 粹客网数据抓取
			CheekerCrawler cheekerCrawler = new CheekerCrawler(
					articleInfoService);
			executor.execute(cheekerCrawler);
			// techweb数据抓取
			TechWebCrawler techWebCrawler = new TechWebCrawler(
					articleInfoService);
			executor.execute(techWebCrawler);
			// 百度百家数据抓取
			BaiduBaijiaCrawler baiduBaijiaCrawler = new BaiduBaijiaCrawler(
					articleInfoService);
			executor.execute(baiduBaijiaCrawler);
			// 网易科技数据抓取
			NetTechnologyCrawler netTechnologyCrawler = new NetTechnologyCrawler(
					articleInfoService);
			executor.execute(netTechnologyCrawler);
			// 虎嗅数据抓取
			HuxiuCrawler huxiuCrawler = new HuxiuCrawler(articleInfoService);
			executor.execute(huxiuCrawler);
			// 爱范儿数据抓取
			IfanrCrawler ifanrCrawler = new IfanrCrawler(articleInfoService);
			executor.execute(ifanrCrawler);
			// 品途数据抓取
			PintuCrawler pintuCrawler = new PintuCrawler(articleInfoService);
			executor.execute(pintuCrawler);
			// 好奇心日报数据抓取
			QdailyCrawler qdailyCrawler = new QdailyCrawler(articleInfoService);
			executor.execute(qdailyCrawler);
			// 雷锋网数据抓取
			LeiPhoneCrawler leiPhoneCrawler = new LeiPhoneCrawler(
					articleInfoService);
			executor.execute(leiPhoneCrawler);
			// 新浪科技数据抓取
			SinaTechnologyCrawler sinaTechnologyCrawler = new SinaTechnologyCrawler(
					articleInfoService);
			executor.execute(sinaTechnologyCrawler);
			// 钛媒体数据抓取
			TmtPostCrawler tmtPostCrawler = new TmtPostCrawler(
					articleInfoService);
			executor.execute(tmtPostCrawler);
		} catch (Exception e) {
			logger.error("crawlerInfo is exception:" + e.toString());
		}
	}

}
