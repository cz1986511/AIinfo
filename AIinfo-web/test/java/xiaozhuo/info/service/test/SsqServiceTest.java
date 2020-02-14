package xiaozhuo.info.service.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.excel.util.CollectionUtils;

import xiaozhuo.info.persist.base.SsqInfo;
import xiaozhuo.info.persist.mapper.SsqInfoMapper;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SsqServiceTest {
	
	@Autowired
	private SsqInfoMapper ssqInfoMapper;
	
	@Test
	public void testList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", 0);
		map.put("limit", 3000);
		List<SsqInfo> allSsq = ssqInfoMapper.selectByParams(map);
		Map<Integer, Integer> map1 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map3 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map4 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map5 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map6 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mapL = new HashMap<Integer, Integer>();
		Integer key = 0;
		for (SsqInfo ssqInfo : allSsq) {
			key = ssqInfo.getH1Num();
			if (null != map1.get(key)) {
				map1.put(key, map1.get(key)+1);
			} else {
				map1.put(key, 1);
			}
			key = ssqInfo.getH2Num();
			if (null != map2.get(key)) {
				map2.put(key, map2.get(key)+1);
			} else {
				map2.put(key, 1);
			}
			key = ssqInfo.getH3Num();
			if (null != map3.get(key)) {
				map3.put(key, map3.get(key)+1);
			} else {
				map3.put(key, 1);
			}
			key = ssqInfo.getH4Num();
			if (null != map4.get(key)) {
				map4.put(key, map4.get(key)+1);
			} else {
				map4.put(key, 1);
			}
			key = ssqInfo.getH5Num();
			if (null != map5.get(key)) {
				map5.put(key, map5.get(key)+1);
			} else {
				map5.put(key, 1);
			}
			key = ssqInfo.getH6Num();
			if (null != map6.get(key)) {
				map6.put(key, map6.get(key)+1);
			} else {
				map6.put(key, 1);
			}
			key = ssqInfo.getlNum();
			if (null != mapL.get(key)) {
				mapL.put(key, mapL.get(key)+1);
			} else {
				mapL.put(key, 1);
			}
		}
		Map<Integer, Integer> sortMap1 = new LinkedHashMap<Integer, Integer>();
		map1.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap1.put(x.getKey(), x.getValue()));
		System.out.print("红球1总数:");
		System.out.println(sortMap1);
		Map<Integer, Integer> sortMap2 = new LinkedHashMap<Integer, Integer>();
		map2.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap2.put(x.getKey(), x.getValue()));
		System.out.print("红球2总数:");
		System.out.println(sortMap2);
		Map<Integer, Integer> sortMap3 = new LinkedHashMap<Integer, Integer>();
		map3.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap3.put(x.getKey(), x.getValue()));
		System.out.print("红球3总数:");
		System.out.println(sortMap3);
		Map<Integer, Integer> sortMap4 = new LinkedHashMap<Integer, Integer>();
		map4.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap4.put(x.getKey(), x.getValue()));
		System.out.print("红球4总数:");
		System.out.println(sortMap4);
		Map<Integer, Integer> sortMap5 = new LinkedHashMap<Integer, Integer>();
		map5.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap5.put(x.getKey(), x.getValue()));
		System.out.print("红球5总数:");
		System.out.println(sortMap5);
		Map<Integer, Integer> sortMap6 = new LinkedHashMap<Integer, Integer>();
		map6.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMap6.put(x.getKey(), x.getValue()));
		System.out.print("红球6总数:");
		System.out.println(sortMap6);
		Map<Integer, Integer> sortMapL = new LinkedHashMap<Integer, Integer>();
		mapL.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMapL.put(x.getKey(), x.getValue()));
		System.out.print("蓝球总数:");
		System.out.println(sortMapL);
		for (int i = 1; i < 6; i++) {
			System.out.print("第"+i+"组:");
			System.out.print(getResultNum(allSsq, 1, i) + ",");
			System.out.print(getResultNum(allSsq, 2, i) + ",");
			System.out.print(getResultNum(allSsq, 3, i) + ",");
			System.out.print(getResultNum(allSsq, 4, i) + ",");
			System.out.print(getResultNum(allSsq, 5, i) + ",");
			System.out.print(getResultNum(allSsq, 6, i) + ",");
			System.out.print(getResultNum(allSsq, 0, i));
			System.out.println();
		}
	}
	
	public Integer getResultNum(List<SsqInfo> allSsq, int flag, int num) {
		Map<Integer, Integer> next1 = getNextLMap(allSsq, allSsq.get(0), 1, flag);
		Map<Integer, Integer> next2 = getNextLMap(allSsq, allSsq.get(1), 2, flag);
		Map<Integer, Integer> next3 = getNextLMap(allSsq, allSsq.get(2), 3, flag);
		Map<Integer, Integer> next4 = getNextLMap(allSsq, allSsq.get(3), 4, flag);
		Map<Integer, Integer> next5 = getNextLMap(allSsq, allSsq.get(4), 5, flag);
		if (num == 2) {
			next2.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
		}
		if (num == 3) {
			next2.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next3.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
		}
		if (num == 4) {
			next2.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next3.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next4.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
		}
		if (num == 5) {
			next2.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next3.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next4.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
			next5.forEach((worlds,value) -> next1.merge(worlds, value, (v1,v2) -> (v1 + v2)));
		}
		Map<Integer, Integer> sort = new LinkedHashMap<Integer, Integer>();
		next1.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sort.put(x.getKey(), x.getValue()));
		List<Integer> resultList = new ArrayList<Integer>(sort.keySet());
		return resultList.get(0);
	}
	
	public Map<Integer, Integer> getNextLMap(List<SsqInfo> list, SsqInfo ssqInfo, int type, int flag) {
		if (CollectionUtils.isEmpty(list) || null == ssqInfo) {
			return null;
		}
		Integer last = 0;
		switch (flag) {
		case 1:
			last = ssqInfo.getH1Num();
			break;
		case 2:
			last = ssqInfo.getH2Num();
			break;
		case 3:
			last = ssqInfo.getH3Num();
			break;
		case 4:
			last = ssqInfo.getH4Num();
			break;
		case 5:
			last = ssqInfo.getH5Num();
			break;
		case 6:
			last = ssqInfo.getH6Num();
			break;
		case 0:
			last = ssqInfo.getlNum();
			break;
		default:
			break;
		}
		Map<Integer, Integer> nextMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sortNextMap = new LinkedHashMap<Integer, Integer>();
		int size = list.size();
		Integer key = 0;
		for (int i = 0; i < size; i++) {
			SsqInfo tempInfo = list.get(i);
			switch(flag) {
			  case 1:
				  if (tempInfo.getH1Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH1Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 2:
				  if (tempInfo.getH2Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH2Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 3:
				  if (tempInfo.getH3Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH3Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 4:
				  if (tempInfo.getH4Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH4Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 5:
				  if (tempInfo.getH5Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH5Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 6:
				  if (tempInfo.getH6Num() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getH6Num();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  case 0:
				  if (tempInfo.getlNum() == last && i > type-1) {
						SsqInfo tempNext = list.get(i-type);
						key = tempNext.getlNum();
						if (null != nextMap.get(key)) {
							nextMap.put(key, nextMap.get(key)+1);
						} else {
							nextMap.put(key, 1);
						}
					}
				  break;
			  default:
				  break;
			}
			
		}
		nextMap.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortNextMap.put(x.getKey(), x.getValue()));
		return nextMap;
	}
	
	public static void main(String[] args) {
		Map<Integer, Integer> map1 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
		map1.put(1, 5);
		map2.put(1,10);
		map2.put(2, 3);
		map1.forEach((key,value) -> map2.merge(key, value, (v1,v2) -> (v1 + v2)));
		System.out.println(1);
	}
}
