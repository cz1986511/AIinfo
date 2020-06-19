package xiaozhuo.info.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xiaozhuo.info.service.util.*;
import xiaozhuo.info.web.common.vo.DateVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuochen
 * @date 2020-06-19 18:16
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class ToolController {

	@Value("${data.token}")
	private String dataToken;

	@RequestMapping(value = "/tool/date", method = RequestMethod.GET)
	@ResponseBody
	public String getDateInfo(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		String token = request.getParameter("token");
		if(!dataToken.equals(token)) {
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
			return json.toJSONString();
		}
		String dateString = request.getParameter("date");
		Date date = new Date();
		String dateKey = CommonTools.getDateString();
		if(!StringUtils.isEmpty(dateString)) {
			date = CommonTools.getStringDate(dateString);
			if(null == date) {
				result.put("status", Constant.ERRORCODE3);
				result.put("msg", Constant.ERRORMSG3);
				return json.toJSONString();
			}
			dateKey = dateString;
		}
		try {
			DateVO dateVO = (DateVO) RedisClient.get(dateKey,
					new TypeReference<DateVO>() {
					});
			if (null != dateVO) {
				result.put("data", dateVO);
			} else {
				Lunar lunar = new Lunar(date);
				DateVO dateResult = new DateVO();
				dateResult.setDateInfo(lunar.getLunarDate());
				dateResult.setLunar(lunar.getLunarDay());
				dateResult.setLunarFestival(lunar.getLunarHoliday());
				dateResult.setLunarSolar(lunar.getSoralTerm());
				dateResult.setCalendarFestival(lunar.getHoliday());
				result.put("data", dateResult);
				RedisClient.set(dateKey, dateResult, 3600);
			}
			result.put("status", Constant.SUCESSCODE);
		} catch (Exception e) {
			log.error("getDateInfo is exception:{}", e.toString());
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

}
