package xiaozhuo.info.service.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RandomSSQ {

	public static void random6Number1() {
		long sysTime = 0L;
		int index = 0;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 1; i < 34; i++) {
			map.put(i, i);
		}
		List<Integer> list = new ArrayList<Integer>();
		while (list.size() < 6) {
			try {
				Thread.sleep(1000);
				sysTime = System.currentTimeMillis() + (System.currentTimeMillis() % 34)
						+ (System.currentTimeMillis() / 34);
				index = Long.valueOf(sysTime % 34).intValue();
				Integer temp = map.get(index);
				if (null != temp) {
					list.add(temp);
					map.remove(index, temp);
				}
			} catch (Exception e) {
				e.toString();
			}
		}
		for (int j = 0; j < list.size(); j++) {
			System.out.print(list.get(j) + " ");
		}
		System.out.println();
	}

	public static void random6Number2() {
		Random random = new Random();
		List<Integer> list = new ArrayList<Integer>();
		while (list.size() < 7) {
			int a = random.nextInt(33);
			list.add(a + 1);
		}
		for (int j = 0; j < list.size(); j++) {
			System.out.print(list.get(j) + " ");
		}
		System.out.println();
	}


	public static void makeMapL(Map<Integer, Integer> map, int temp) {
		Integer k = Integer.valueOf(temp);
		if (map.get(k) != null) {
			int n = map.get(k);
			map.put(k, n + 1);
		} else {
			map.put(k, 1);
		}
	}

	public static void makeMapH(Map<Integer, Integer> map, String temp) {
		String[] array = temp.split(" ");
		for (int i = 0; i < array.length; i++) {
			Integer k = Integer.valueOf(array[i]);
			if (map.get(k) != null) {
				int n = map.get(k);
				map.put(k, n + 1);
			} else {
				map.put(k, 1);
			}
		}
	}

	public static void makeAddMap(Map<Integer, Integer> map, String temp, int num) {
		String[] array = temp.split(" ");
		for (int i = 0; i < array.length; i++) {
			Integer k = Integer.valueOf(array[i]);
			if (map.get(k) != null) {
				int n = map.get(k);
				map.put(k, n + num);
			} else {
				map.put(k, num);
			}
		}
	}

	public static void makeAddLMap(Map<Integer, Integer> map, int temp, int num) {
		if (map.get(temp) != null) {
			int n = map.get(temp);
			map.put(temp, n + num);
		} else {
			map.put(temp, num);
		}
	}

	public static List<Integer> makeLast100(Sheet sheet, Map<Integer, Integer> map) {
		int lastRowIndex = sheet.getLastRowNum();
		for (int i = 1; i < lastRowIndex + 1; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(1);
			makeMapH(map, cell.getStringCellValue());
		}
		for (int j = lastRowIndex; j > lastRowIndex - 4; j--) {
			int num = 4;
			Row row = sheet.getRow(j);
			Cell cell = row.getCell(1);
			makeAddMap(map, cell.getStringCellValue(), num);
			num--;
		}
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		List<Integer> listResult = resultMap.keySet().stream().collect(Collectors.toList());
		return listResult;
	}

	public static List<Integer> makeALL(Sheet sheet, Map<Integer, Integer> map) {
		int lastRowIndex = sheet.getLastRowNum();
		for (int i = 1; i < lastRowIndex + 1; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(1);
			makeMapH(map, cell.getStringCellValue());
		}
		for (int j = lastRowIndex; j > lastRowIndex - 10; j--) {
			int num = 40;
			Row row = sheet.getRow(j);
			Cell cell = row.getCell(1);
			makeAddMap(map, cell.getStringCellValue(), num);
			num = num - 4;
		}
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		List<Integer> listResult = resultMap.keySet().stream().collect(Collectors.toList());
		return listResult;
	}

	public static List<Integer> makeLANQ(Sheet sheet, Map<Integer, Integer> map) {
		int lastRowIndex = sheet.getLastRowNum();
		for (int i = 1; i < lastRowIndex + 1; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(2);
			makeMapL(map, (int) cell.getNumericCellValue());
		}
		for (int j = lastRowIndex; j > lastRowIndex - 10; j--) {
			int num = 10;
			Row row = sheet.getRow(j);
			Cell cell = row.getCell(2);
			makeAddLMap(map, (int) cell.getNumericCellValue(), num);
			num--;
		}
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		List<Integer> listResult = resultMap.keySet().stream().collect(Collectors.toList());
		return listResult;
	}

	public static void makeRandomSsq2(Sheet sheet) {
		Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();
		int lastRowIndex = sheet.getLastRowNum();
		for (int i = 1; i < lastRowIndex + 1; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(1);
			String temp = cell.getStringCellValue();
			String[] array = temp.split(" ");
			for (int j = 0; j < array.length; j++) {
				Integer k = Integer.valueOf(array[j]);
				Map<Integer, Integer> tempMap = map.get(j);
				if (null != tempMap) {
					if (tempMap.get(k) != null) {
						int n = tempMap.get(k);
						tempMap.put(k, n + 1);
					} else {
						tempMap.put(k, 1);
					}
				} else {
					tempMap = new HashMap<Integer, Integer>();
					tempMap.put(k, 1);
				}
				map.put(j, tempMap);
			}
		}
//		Map<Integer, Integer> resultMap1 = 
		map.get(0).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);
		System.out.println("=====");
		map.get(1).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);
		System.out.println("=====");
		map.get(2).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);
		System.out.println("=====");
		map.get(3).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);
		System.out.println("=====");
		map.get(4).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);
		System.out.println("=====");
		map.get(5).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3)
				.forEach(System.out::println);

//				collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//		List<Integer> listResult1 = resultMap1.keySet().stream().collect(Collectors.toList());
//		listResult1.forEach(n -> System.out.print(n + " "));
	}

}
