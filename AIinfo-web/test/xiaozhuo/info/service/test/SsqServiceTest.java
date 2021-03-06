package xiaozhuo.info.service.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.excel.util.CollectionUtils;
import xiaozhuo.info.persist.base.ConsumeAmount;
import xiaozhuo.info.persist.base.SsqInfo;
import xiaozhuo.info.persist.mapper.SsqInfoMapper;
import xiaozhuo.info.service.ConsumeAmountService;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SsqServiceTest {
	
	@Autowired
	private SsqInfoMapper ssqInfoMapper;

	@Autowired
	private ConsumeAmountService consumeAmountService;

	@Test
	public void testSaveConsumeAmount() {
		ConsumeAmount amount = new ConsumeAmount();
		amount.setAmount(1188L);
		amount.setYear(2020);
		amount.setMonth(8);
		amount.setDay(11);
		amount.setType(1);
		//consumeAmountService.saveConsumeAmount(amount);
		Map<String, Object> map = consumeAmountService.getAmountData(new HashMap<>());
	}
	
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
				map1.put(key, map1.get(key) + 1);
			} else {
				map1.put(key, 1);
			}
			key = ssqInfo.getH2Num();
			if (null != map2.get(key)) {
				map2.put(key, map2.get(key) + 1);
			} else {
				map2.put(key, 1);
			}
			key = ssqInfo.getH3Num();
			if (null != map3.get(key)) {
				map3.put(key, map3.get(key) + 1);
			} else {
				map3.put(key, 1);
			}
			key = ssqInfo.getH4Num();
			if (null != map4.get(key)) {
				map4.put(key, map4.get(key) + 1);
			} else {
				map4.put(key, 1);
			}
			key = ssqInfo.getH5Num();
			if (null != map5.get(key)) {
				map5.put(key, map5.get(key) + 1);
			} else {
				map5.put(key, 1);
			}
			key = ssqInfo.getH6Num();
			if (null != map6.get(key)) {
				map6.put(key, map6.get(key) + 1);
			} else {
				map6.put(key, 1);
			}
			key = ssqInfo.getlNum();
			if (null != mapL.get(key)) {
				mapL.put(key, mapL.get(key) + 1);
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
			System.out.print("第" + i + "组:");
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
		//createExcel();
		for(int i = 1; i < 6; i++) {
			Map<Integer, Integer> mapH = new HashMap<>();
			Map<Integer, Integer> mapL = new HashMap<>();
			long time = System.currentTimeMillis();
			int result = 0;
			int lnum = 0;
			for(long k = 0; k < 88888888L; k++) {
				int count = 0;
				while(count < 6) {
					result = (int) (Math.random() * 32) + 1;
					if(null != mapH.get(result)) {
						mapH.put(result, mapH.get(result) + 1);
					} else {
						mapH.put(result, 1);
					}
					count++;
				}
				lnum = (int)(Math.random() * 14) + 1;
				if(null != mapL.get(lnum)) {
					mapL.put(lnum, mapL.get(lnum) + 1);
				} else {
					mapL.put(lnum, 1);
				}
			}
			Map<Integer, Integer> sortMapH = new LinkedHashMap<Integer, Integer>();
			mapH.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMapH.put(x.getKey(), x.getValue()));

			Map<Integer, Integer> sortMapL = new LinkedHashMap<Integer, Integer>();
			mapL.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMapL.put(x.getKey(), x.getValue()));

			System.out.println("第" + i + "次:");
			System.out.println("time:" + (System.currentTimeMillis()-time));

			Iterator<Map.Entry<Integer, Integer>> iterator = sortMapH.entrySet().iterator();
			int countH = 0;
			System.out.print("红球:");
			while(iterator.hasNext()) {
				Map.Entry<Integer, Integer> entry = iterator.next();
				System.out.print(entry.getKey() + "=" + entry.getValue() + ",");
				countH++;
				if(countH == 6) {
					break;
				}
			}
			System.out.println();
			Iterator<Map.Entry<Integer, Integer>> iteratorL = sortMapL.entrySet().iterator();
			int countL = 0;
			System.out.print("蓝球:");
			while(iteratorL.hasNext()) {
				Map.Entry<Integer, Integer> entryL = iteratorL.next();
				System.out.println(entryL.getKey() + "=" + entryL.getValue());
				countL++;
				if(countL == 1) {
					break;
				}
			}
		}
	}

	public static void createExcel() {
		String fileName = "/Users/zhuochen/work/excel.xlsx";
		File file = new File(fileName);
		FileOutputStream fout = null;
		try{
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("多结算体系导出");
			for(int rowNo = 0; rowNo < 5; rowNo++) {
				HSSFRow row_ = sheet.createRow(rowNo);
				if(null == row_) {
					System.out.println("row is null");
				}
				HSSFCell cell_;
				switch (rowNo) {
					case 0:
						cell_ = row_.createCell(0);
						cell_.setCellValue("多结算体系导出");
						sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
						break;
					case 1:
						cell_ = row_.createCell(0);
						cell_.setCellValue("商户:");
						cell_ = row_.createCell(1);
						cell_.setCellValue("多级配送分店1");
						cell_ = row_.createCell(3);
						cell_.setCellValue("导出人:");
						cell_ = row_.createCell(4);
						cell_.setCellValue("张三");
						break;
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.flush();
			fout.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e1) {
				}
			}
		}
	}


}
