package xiaozhuo.info.service.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import xiaozhuo.info.service.ArticleInfoService;
import xiaozhuo.info.service.util.crawler.GeekParkCrawler;
import xiaozhuo.info.service.util.crawler.GuoKrCrawler;
import xiaozhuo.info.service.util.crawler.HuxiuCrawler;
import xiaozhuo.info.service.util.crawler.IfanrCrawler;
import xiaozhuo.info.service.util.crawler.IheimaCrawler;
import xiaozhuo.info.service.util.crawler.LeiPhoneCrawler;
import xiaozhuo.info.service.util.crawler.NetTechnologyCrawler;
import xiaozhuo.info.service.util.crawler.PintuCrawler;
import xiaozhuo.info.service.util.crawler.QdailyCrawler;
import xiaozhuo.info.service.util.crawler.TechWebCrawler;
import xiaozhuo.info.service.util.crawler.ThirtySixKrCrawler;
import xiaozhuo.info.service.util.crawler.TmtPostCrawler;
import xiaozhuo.info.service.util.crawler.YiCaiCrawler;

/**
 * @author zhuochen
 */

@Component
@Configurable
@EnableScheduling
@Service
@Slf4j
public class Crawler {

	@Autowired
	private ArticleInfoService articleInfoService;

	private ThreadPoolExecutor executor;

	public void init() {
		this.executor = new ThreadPoolExecutor(2, 5, 2000,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	@Scheduled(cron = "0 7 0/1 * * ?")
	public void crawlerInfo() {
		try {
			if (null == executor) {
				init();
			}
			// 第一财经数据抓取
			YiCaiCrawler yiCaiCrawler = new YiCaiCrawler(articleInfoService);
			executor.execute(yiCaiCrawler);
			// i黑马数据抓取
			IheimaCrawler iheimaCrawler = new IheimaCrawler(articleInfoService);
			executor.execute(iheimaCrawler);
			// 果壳数据抓取
			GuoKrCrawler guoKrCrawler = new GuoKrCrawler(articleInfoService);
			executor.execute(guoKrCrawler);
			// 极客公园数据抓取
			GeekParkCrawler geekParkCrawler = new GeekParkCrawler(
					articleInfoService);
			executor.execute(geekParkCrawler);
			// 36氪数据抓取
			ThirtySixKrCrawler thritySixCrawler = new ThirtySixKrCrawler(
					articleInfoService);
			executor.execute(thritySixCrawler);
			// 移动互联数据抓取
			TechWebCrawler techWebCrawler = new TechWebCrawler(
					articleInfoService);
			executor.execute(techWebCrawler);
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
			// 钛媒体数据抓取
			TmtPostCrawler tmtPostCrawler = new TmtPostCrawler(
					articleInfoService);
			executor.execute(tmtPostCrawler);
		} catch (Exception e) {
			log.error("crawlerInfo is exception:{}", e.toString());
		}
	}

}
