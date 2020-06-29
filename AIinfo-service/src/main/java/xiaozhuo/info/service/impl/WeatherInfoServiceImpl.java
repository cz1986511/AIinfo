package xiaozhuo.info.service.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.WeatherInfo;
import xiaozhuo.info.persist.mapper.WeatherInfoMapper;
import xiaozhuo.info.service.WeatherInfoService;

@Service
public class WeatherInfoServiceImpl implements WeatherInfoService {

	@Autowired
	private WeatherInfoMapper weatherInfoMapper;

	@Override
	public int addWeatherInfo(WeatherInfo weatherInfo) {
		if (null != weatherInfo) {
			return weatherInfoMapper.insertSelective(weatherInfo);
		}
		return 0;
	}

	@Override
	public List<WeatherInfo> getWeatherInfos(Map<String, Object> map) {
		if (!CollectionUtils.isEmpty(map)) {
			return weatherInfoMapper.selectWeathersByParams(map);
		}
		return Lists.newArrayList();
	}

}
