package xiaozhuo.info.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import xiaozhuo.info.persist.base.OilInfo;
import xiaozhuo.info.service.OilInfoService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
@Component
@Configurable
@EnableScheduling
@Service
public class OilInfoUtil {

	private static Logger logger = LoggerFactory.getLogger(OilInfoUtil.class);
	@Autowired
	private OilInfoService oilInfoService;

	@SuppressWarnings({ "resource" })
	@Scheduled(cron = "0 17 7 * * ?")
	public void getTodayOilInfo() {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String restUrl = "http://api.jisuapi.com/oil/query?appkey=a6a2ebcd0ed0a899&province=四川";
			HttpGet getMethod = new HttpGet(restUrl);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					String responseBody = EntityUtils.toString(
							response.getEntity(), "UTF-8");
					JSONObject jsonObject = (JSONObject) JSON.parseObject(
							responseBody).get("result");
					String oilKey = "oilKey";
					RedisClient.set(oilKey, jsonObject, 86400);
					OilInfo oilInfo = new OilInfo();
					oilInfo.setOil0(jsonObject.getString("oil0"));
					oilInfo.setOil89(jsonObject.getString("oil89"));
					oilInfo.setOil90(jsonObject.getString("oil90"));
					oilInfo.setOil92(jsonObject.getString("oil92"));
					oilInfo.setOil93(jsonObject.getString("oil93"));
					oilInfo.setOil95(jsonObject.getString("oil95"));
					oilInfo.setOil97(jsonObject.getString("oil97"));
					oilInfo.setProvince("四川");
					oilInfo.setUpdateTime(getDateString(jsonObject
							.getString("updatetime")));
					oilInfoService.addOilInfo(oilInfo);
				}
			}
		} catch (Exception e) {
			logger.error("getTodayOilInfo is exception:" + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		logger.error("getTodayOilInfo is null");
	}

	private Date getDateString(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
