package xiaozhuo.info.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringDesc {

	public static String stringReverse1(String s) {
		char[] charArr = s.toCharArray();
		char swap;
		for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
			swap = charArr[i];
			charArr[i] = charArr[j];
			charArr[j] = swap;
		}
		return String.valueOf(charArr);
	}

	public static String stringReverse2(String s) {
		StringBuffer stringBuffer = new StringBuffer(s);
		stringBuffer.reverse();
		return stringBuffer.toString();
	}

	public static void main(String[] args) {
		for (int m = 0; m < 5; m++) {
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
					sysTime = System.currentTimeMillis() + (System.currentTimeMillis() % 34) + (System.currentTimeMillis() / 34);
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
	}

}
