package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.WeatherInfo;

public interface WeatherInfoMapper {
	int deleteByPrimaryKey(Long id);

	int insert(WeatherInfo weatherInfo);

	int insertSelective(WeatherInfo weatherInfo);

	WeatherInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(WeatherInfo weatherInfo);

	int updateByPrimaryKey(WeatherInfo weatherInfo);

	List<WeatherInfo> selectWeathersByParams(Map<String, Object> map);
}