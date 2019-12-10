package xiaozhuo.info.service.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode1000 {
	
	public static void main(String[] args) {
		 String[] products = {};
	}
	
	/**
	 * 1051
	 * Date:2019-11-28 17:51
	 * Time:1ms
	 * Memory:34.7MB
	 */
	public int heightChecker(int[] heights) {
        int[] nheights = heights.clone();
		 Arrays.sort(nheights);
		 int len = heights.length;
		 int result = 0;
		 for (int i = 0; i < len; i++) {
			 if (heights[i] != nheights[i]) {
				 result++;
			 }
		 }
        return result;
    }
}
