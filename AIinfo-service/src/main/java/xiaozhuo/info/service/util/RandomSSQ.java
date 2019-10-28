package xiaozhuo.info.service.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
		while (list.size() < 6) {
			int a = random.nextInt(34);
			list.add(a);
		}
		for (int j = 0; j < list.size(); j++) {
			System.out.print(list.get(j) + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		random6Number2();
	}

}
