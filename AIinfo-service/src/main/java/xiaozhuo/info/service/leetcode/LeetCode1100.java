package xiaozhuo.info.service.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeetCode1100 {
	
	public static void main(String[] args) {
		 int n = 37;
		 System.out.println(tribonacci(n));
	}
	
	/**
	 * 1137
	 * Date:2019-11-27 17:30
	 * Time:0ms
	 * Memory:33.2MB
	 */
	public static int tribonacci(int n) {
        if(n <= 1) return n;
        int t0 = 0;
        int t1 = 1;
        int t2 = 1;
        for(int i = 3; i <= n; i++){
            int tmp = t0 + t1 + t2;
            t0 = t1;
            t1 = t2;
            t2 = tmp;
        }
        return t2;
	}
	
	/**
	 * 1170
	 * Date:2019-11-27 16:20
	 * Time:18ms
	 * Memory:38MB
	 */
	public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int wlen = words.length;
		int qlen = queries.length;
		int[] result = new int[qlen];
		int[] wInt = makeStringToInteger(words);
		int[] qInt = makeStringToInteger(queries);
		Arrays.sort(wInt);
		for (int i = 0; i < qlen; i++) {
			int temp = qInt[i];
			int rNum = 0;
			for (int j = 0; j < wlen; j++) {
				if (wInt[j] > temp) {
					rNum = wlen - j;
					break;
				}
			}
			result[i] = rNum;
		}
        return result;
    }
    
    private static int[] makeStringToInteger(String[] words) {
		int len = words.length;
		int[] f = new int[len];
		for (int i = 0; i < len; i++) {
			String str = words[i];
			char[] strArray = str.toCharArray();
			int strLen = strArray.length;
			char temp = 'z';
			int num = 0;
			for (int j = 0; j < strLen; j++) {
				if (strArray[j] < temp) {
					num = 1;
					temp = strArray[j];
				} else if(strArray[j] == temp) {
					num += 1;
				}
			}
			f[i] = num;
		}
		return f;
	}
	
	/**
	 * 1189
	 * Date:2019-11-27 12:00
	 * Time:6ms
	 * Memory:35.9MB
	 */
	public int maxNumberOfBalloons(String text) {
        String balloon = "balon";
		char[] arr = text.toCharArray();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (char c : arr) {
			String key = String.valueOf(c);
			if (balloon.contains(key)) {
				if (null != map.get(key)) {
					int value = map.get(key) + 1;
					map.put(key, value);
				} else {
					map.put(key, 1);
				}
			}
		}
		int result = 0;
		Integer tempA = 0;
		Integer tempB = 0;
		Integer tempN = 0;
		Integer tempL = 0;
		Integer tempO = 0;
		tempA = map.get("a");
		tempB = map.get("b");
		tempN = map.get("n");
		tempL = map.get("l");
		tempO = map.get("o");
		if (null == tempA || null == tempB || null == tempN || null == tempL || null == tempO) {
			return result;
		} else {
			result = tempA;
			if (tempB < result) {
				result = tempB;
			}
			if (tempN < result) {
				result = tempN;
			}
			if (tempL/2 < result) {
				result = tempL/2;
			}
			if (tempO/2 < result) {
				result = tempO/2;
			}
		}
        return result;
    }
	
}
