package xiaozhuo.info.service.util;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import xiaozhuo.info.persist.base.OilInfo;
import xiaozhuo.info.service.OilInfoService;

/**
 * @author Vin
 */
@Component
@Configurable
@EnableScheduling
@Service
@Slf4j
public class OilInfoUtil {

	@Autowired
	private OilInfoService oilInfoService;

	@Scheduled(cron = "0 17 7 * * ?")
	public void getTodayOilInfo() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			String restUrl = "http://api.jisuapi.com/oil/query?appkey=a6a2ebcd0ed0a899&province=四川";
			HttpGet getMethod = new HttpGet(restUrl);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response && response.getStatusLine().getStatusCode() == 200) {
				String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject jsonObject = (JSONObject) JSON.parseObject(responseBody).get("result");

				String dateString = jsonObject.getString("updatetime");
				if (null != dateString) {
					jsonObject.put("updatetime", dateString.substring(0, 10));
				}
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
				oilInfo.setUpdateTime(CommonTools.getDateString(dateString));
				oilInfoService.addOilInfo(oilInfo);
			}
		} catch (Exception e) {
			log.error("getTodayOilInfo is exception:{}", e.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	}
}
