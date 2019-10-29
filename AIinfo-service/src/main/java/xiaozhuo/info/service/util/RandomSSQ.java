package xiaozhuo.info.service.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public static void main(String[] args) {
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mapL = new HashMap<Integer, Integer>();
		Map<Integer, Integer> map3 = new HashMap<Integer, Integer>();
		try {
			String filePath = "d:\\ssq.xlsx";
			File excel = new File(filePath);
			Workbook wb;
			wb = new XSSFWorkbook(excel);
			Sheet sheet2 = wb.getSheetAt(2);
			List<Integer> list100 = makeLast100(sheet2, map2);
			Sheet sheet3 = wb.getSheetAt(3);
			List<Integer> listALL = makeALL(sheet3, map3);
			List<Integer> listL = makeLANQ(sheet3, mapL);
			listL.forEach(n -> {
				list100.forEach(a -> {
					System.out.print(a + " ");
				});
				System.out.print(n);
				System.out.println();
				listALL.forEach(b -> {
					System.out.print(b + " ");
				});
				System.out.print(n);
				System.out.println();
			});
		} catch (Exception e) {
			e.toString();
		}
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
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5).
				collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5).
				collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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
		Map<Integer, Integer> resultMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5).
				collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		List<Integer> listResult = resultMap.keySet().stream().collect(Collectors.toList());
		return listResult;
	}

}
