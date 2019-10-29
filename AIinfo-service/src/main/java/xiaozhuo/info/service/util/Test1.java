package xiaozhuo.info.service.util;

import java.util.HashMap;
import java.util.Map;

public class Test1 {
	
	public static void main(String[] args) {
		String s1 = "02 06 08 17 28 30";
		String s2 = "04 10 15 19 21 23";
		String s3 = "02 06 15 21 30 31";
		String s4 = "10 12 27 31 32 33";
		String s5 = "04 20 22 24 26 33";
		String s6 = "12 14 15 25 28 31";
		String s7 = "03 05 11 28 32 33";
		String s8 = "01 08 09 13 16 33";
		String s9 = "04 10 11 14 15 29";
		String s10 = "02 04 09 11 12 30";
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		makeMap(map, s1);
		makeMap(map, s2);
		makeMap(map, s3);
		makeMap(map, s4);
		makeMap(map, s5);
		makeMap(map, s6);
		makeMap(map, s7);
		makeMap(map, s8);
		makeMap(map, s9);
		makeMap(map, s10);
		for (int k = 1; k < 34; k++) {
			if (null == map.get(k)) {
				System.out.println(k + ":0");
			} else {
				System.out.println(k + ":" + map.get(k));
			}
		}
	}
	
	public static void makeMap(Map<Integer, Integer> map, String temp) {
		String[] array = temp.split(" ");
		for (int i = 0; i < array.length; i++) {
			Integer k = Integer.valueOf(array[i]);
			if(map.get(k) != null) {
				int n = map.get(k);
				map.put(k, n+1);
			} else {
				map.put(k, 1);
			}
		}
	}
}
