package xiaozhuo.info.service.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import xiaozhuo.info.persist.base.SsqInfo;
import xiaozhuo.info.persist.mapper.SsqInfoMapper;

@Component
@Configurable
@EnableScheduling
@Service
@Slf4j
public class SsqInfoUtil {
	
	@Autowired
	private SsqInfoMapper ssqInfoMapper;
	
	@Scheduled(cron = "0 17 5 * * ?")
	public void scheduleGetSsqInfo() {
		getTodaySsqInfo(null);
	}

	public void getTodaySsqInfo(String issueno) {
		log.info("getTodaySsqInfo start:======[{}]", LocalDate.now());
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			String restUrl = "https://api.jisuapi.com/caipiao/query?appkey=a6a2ebcd0ed0a899&caipiaoid=11&issueno=";
			if (null != issueno) {
				restUrl += issueno;
			}
			HttpGet getMethod = new HttpGet(restUrl);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response && response.getStatusLine().getStatusCode() == 200) {
				String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject jsonObject = (JSONObject) JSON.parseObject(responseBody).get("result");
				SsqInfo ssqInfo = new SsqInfo();
				ssqInfo.setStatus(0);
				DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String openDate = jsonObject.getString("opendate");
				ssqInfo.setOpenDate(LocalDate.parse(openDate, dtf3));
				String deadLine = jsonObject.getString("deadline");
				ssqInfo.setDeadLine(LocalDate.parse(deadLine, dtf3));
				int qid = Integer.valueOf(jsonObject.getString("issueno"));
				ssqInfo.setQid(qid);
				int cpid = jsonObject.getIntValue("caipiaoid");
				ssqInfo.setCpid(cpid);
				long saleAmount = jsonObject.getLongValue("saleamount");
				ssqInfo.setSaleAmount(String.valueOf(saleAmount));
				String totalMoney = jsonObject.getString("totalmoney");
				ssqInfo.setTotalMoney(totalMoney);
				String referNumber = jsonObject.getString("refernumber");
				ssqInfo.setlNum(Integer.valueOf(referNumber));
				String numbers = jsonObject.getString("number");
				String[] array = numbers.split(" ");
				ssqInfo.setH1Num(Integer.valueOf(array[0]));
				ssqInfo.setH2Num(Integer.valueOf(array[1]));
				ssqInfo.setH3Num(Integer.valueOf(array[2]));
				ssqInfo.setH4Num(Integer.valueOf(array[3]));
				ssqInfo.setH5Num(Integer.valueOf(array[4]));
				ssqInfo.setH6Num(Integer.valueOf(array[5]));
				try {
					JSONArray prizeArray = jsonObject.getJSONArray("prize");
					for (int j = 0; j < prizeArray.size(); j++) {
						JSONObject prizeObject = (JSONObject) prizeArray.get(j);
						if ("一等奖".equals(prizeObject.getString("prizename"))) {
							ssqInfo.setP1Num(prizeObject.getIntValue("num"));
							ssqInfo.setP1Bonus(String.valueOf(prizeObject.getIntValue("singlebonus")));
						}
						if ("二等奖".equals(prizeObject.getString("prizename"))) {
							ssqInfo.setP2Num(prizeObject.getIntValue("num"));
							ssqInfo.setP2Bonus(String.valueOf(prizeObject.getIntValue("singlebonus")));
						}
					}
				} catch (Exception e) {
					log.error("prize is false");
				}
				SsqInfo tempInfo = ssqInfoMapper.selectByQid(qid);
				if (null == tempInfo) {
					ssqInfoMapper.insertSelective(ssqInfo);
				} else {
					ssqInfo.setId(tempInfo.getId());
					ssqInfoMapper.updateByPrimaryKeySelective(ssqInfo);
				}
			}
		} catch (Exception e) {
			log.error("getTodaySsqInfo is exception:[{}]",e.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("getTodaySsqInfo end:======");
	}
	
	public static void main(String[] args) {
		String issueno = "2018107";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			String restUrl = "https://api.jisuapi.com/caipiao/query?appkey=a6a2ebcd0ed0a899&caipiaoid=11&issueno=";
			if (null != issueno) {
				restUrl += issueno;
			}
			HttpGet getMethod = new HttpGet(restUrl);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response && response.getStatusLine().getStatusCode() == 200) {
				String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject jsonObject = (JSONObject) JSON.parseObject(responseBody).get("result");
				SsqInfo ssqInfo = new SsqInfo();
				ssqInfo.setStatus(0);
				DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String openDate = jsonObject.getString("opendate");
				ssqInfo.setOpenDate(LocalDate.parse(openDate, dtf3));
				String deadLine = jsonObject.getString("deadline");
				ssqInfo.setDeadLine(LocalDate.parse(deadLine, dtf3));
				int qid = Integer.valueOf(jsonObject.getString("issueno"));
				ssqInfo.setQid(qid);
				int cpid = jsonObject.getIntValue("caipiaoid");
				ssqInfo.setCpid(cpid);
				long saleAmount = jsonObject.getLongValue("saleamount");
				ssqInfo.setSaleAmount(String.valueOf(saleAmount));
				String totalMoney = jsonObject.getString("totalmoney");
				ssqInfo.setTotalMoney(totalMoney);
				String referNumber = jsonObject.getString("refernumber");
				ssqInfo.setlNum(Integer.valueOf(referNumber));
				String numbers = jsonObject.getString("number");
				String[] array = numbers.split(" ");
				ssqInfo.setH1Num(Integer.valueOf(array[0]));
				ssqInfo.setH2Num(Integer.valueOf(array[1]));
				ssqInfo.setH3Num(Integer.valueOf(array[2]));
				ssqInfo.setH4Num(Integer.valueOf(array[3]));
				ssqInfo.setH5Num(Integer.valueOf(array[4]));
				ssqInfo.setH6Num(Integer.valueOf(array[5]));
				try {
					JSONArray prizeArray = jsonObject.getJSONArray("prize");
					for (int j = 0; j < prizeArray.size(); j++) {
						JSONObject prizeObject = (JSONObject) prizeArray.get(j);
						if ("一等奖".equals(prizeObject.getString("prizename"))) {
							ssqInfo.setP1Num(prizeObject.getIntValue("num"));
							ssqInfo.setP1Bonus(String.valueOf(prizeObject.getIntValue("singlebonus")));
						}
						if ("二等奖".equals(prizeObject.getString("prizename"))) {
							ssqInfo.setP2Num(prizeObject.getIntValue("num"));
							ssqInfo.setP2Bonus(String.valueOf(prizeObject.getIntValue("singlebonus")));
						}
					}
				} catch (Exception e) {
					log.error("prize is false");
				}
			}
		} catch (Exception e) {
			log.error("getTodaySsqInfo is exception:[{}]", e.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	}
	
}
