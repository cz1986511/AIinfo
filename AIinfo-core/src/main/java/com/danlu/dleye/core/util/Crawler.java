package com.danlu.dleye.core.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.ArticleInfoManager;

public class Crawler {

    private static Logger logger = LoggerFactory.getLogger(Crawler.class);

    @Autowired
    private ArticleInfoManager articleInfoManager;
    @Autowired
    private DleyeSwith dleyeSwith;

    private ThreadPoolExecutor executor;

    public void init() {
        this.executor = new ThreadPoolExecutor(2, dleyeSwith.getMaxPool(), 2000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void crawlerInfo() {
        try {
            if (null == executor) {
                init();
            }
            // 虎嗅数据抓取
            HuxiuCrawler huxiuCrawler = new HuxiuCrawler(articleInfoManager);
            executor.execute(huxiuCrawler);
            // 爱范儿数据抓取
            IfanrCrawler ifanrCrawler = new IfanrCrawler(articleInfoManager);
            executor.execute(ifanrCrawler);
            // 品途数据抓取
            PintuCrawler pintuCrawler = new PintuCrawler(articleInfoManager);
            executor.execute(pintuCrawler);
            // 好奇心日报数据抓取
            QdailyCrawler qdailyCrawler = new QdailyCrawler(articleInfoManager);
            executor.execute(qdailyCrawler);
            // 雷锋网数据抓取
            LeiPhoneCrawler leiPhoneCrawler = new LeiPhoneCrawler(articleInfoManager);
            executor.execute(leiPhoneCrawler);
            // 新浪科技数据抓取
            SinaTechnologyCrawler sinaTechnologyCrawler = new SinaTechnologyCrawler(
                articleInfoManager);
            executor.execute(sinaTechnologyCrawler);
            // 钛媒体数据抓取
            TmtPostCrawler tmtPostCrawler = new TmtPostCrawler(articleInfoManager);
            executor.execute(tmtPostCrawler);
            // i黑马数据抓取
            IheimaCrawler iheimaCrawler = new IheimaCrawler(articleInfoManager);
            executor.execute(iheimaCrawler);
            // 粹客网数据抓取
            CheekerCrawler cheekerCrawler = new CheekerCrawler(articleInfoManager);
            executor.execute(cheekerCrawler);
            // techweb数据抓取
            TechWebCrawler techWebCrawler = new TechWebCrawler(articleInfoManager);
            executor.execute(techWebCrawler);
            // 百度百家数据抓取
            BaiduBaijiaCrawler baiduBaijiaCrawler = new BaiduBaijiaCrawler(articleInfoManager);
            executor.execute(baiduBaijiaCrawler);
            // 网易科技数据抓取
            NetTechnologyCrawler netTechnologyCrawler = new NetTechnologyCrawler(articleInfoManager);
            executor.execute(netTechnologyCrawler);
        } catch (Exception e) {
            logger.error("crawlerInfo is exception:" + e.toString());
        }
    }

}
