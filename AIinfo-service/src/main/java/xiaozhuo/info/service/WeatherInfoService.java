package xiaozhuo.info.service;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.WeatherInfo;

public interface WeatherInfoService {

	int addWeatherInfo(WeatherInfo weatherInfo);

	List<WeatherInfo> getWeatherInfos(Map<String, Object> map);

}
