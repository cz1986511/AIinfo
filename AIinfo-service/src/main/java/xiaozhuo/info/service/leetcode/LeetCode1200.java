package xiaozhuo.info.service.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeetCode1200 {
	
	public static void main(String[] args) {
	}
	
	/**
	 * 1200
	 * Date:2019-11-27 10:40
	 * Time:20ms
	 * Memory:51.2MB
	 */
	public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr);
		int len = arr.length;
		int min = 0;
		List<List<Integer>> resultList = new ArrayList<List<Integer>>();
		for (int i = 0; i < len - 1; i++) {
			int temp = arr[i+1] - arr[i];
			if (min != 0) {
				if (temp < min) {
					List<Integer> inList = new ArrayList<Integer>();
					inList.add(arr[i]);
					inList.add(arr[i+1]);
					resultList.clear();
					resultList.add(inList);
                    min = temp;
				} else if (temp == min) {
					List<Integer> inList = new ArrayList<Integer>();
					inList.add(arr[i]);
					inList.add(arr[i+1]);
					resultList.add(inList);
				}
			} else {
				min = temp;
				List<Integer> inList = new ArrayList<Integer>();
				inList.add(arr[i]);
				inList.add(arr[i+1]);
				resultList.add(inList);
			}
		}
        return resultList;
    }
	
	/**
	 * 1207
	 * Date:2019-11-27 10:00
	 */
	public boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int value;
		for(int key : arr) {
			if (null != map.get(key)) {
				value = map.get(key) + 1;
				map.put(key, value);
			} else {
				map.put(key, 1);
			}
		}
		List<Integer> values = new ArrayList<Integer>();
		for (int temp : map.values()) {
			if (values.contains(temp)) {
				return false;
			} else {
				values.add(temp);
			}
		}
		return true;
    }
	
	/**
	 * 1221
	 * Date:2019-11-26
	 */
	public int balancedStringSplit(String s) {
        char[] charArray = s.toCharArray();
		int length = charArray.length;
		int result = 0;
		char temp;
		List<Character> rList = new ArrayList<Character>();
		List<Character> lList = new ArrayList<Character>();
		for (int i = 0; i < length; i++) {
			temp = charArray[i];
			if ("R".equals(String.valueOf(temp))) {
				rList.add(temp);
			} else {
				lList.add(temp);
			}
			if (rList.size() > 0 && rList.size() == lList.size()) {
				result += 1;
				rList.clear();
				lList.clear();
			}
		}
        return result;
    }
	
	/**
	 * 1252
	 * Date:2019-11-26
	 */
	public int oddCells(int n, int m, int[][] indices) {
        int[][] start = new int[n][m];
		int result = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                start[i][j] = 0;
            }
        }
        int len = indices.length;
        for (int k = 0; k < len; k++) {
        	int[] temp = indices[k];
        	int hang = temp[0];
        	int lie = temp[1];
        	for(int i = 0; i < m; i++) {
        		start[hang][i] += 1;
        	}
        	for(int j = 0; j < n; j++) {
        		start[j][lie] += 1;
        	}
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(start[i][j]%2 != 0) {
                	result += 1;
                }
            }
        }
        return result;
    }
	
	/**
	 * 1260
	 * Date:2019-11-26
	 */
	public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int n = grid.length;
		int m = grid[0].length;
		for (int times = 0; times < k; times++) {
			int[][] newGrid = new int[n][m];
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < m - 1; j++) {
					newGrid[i][j+1] = grid[i][j];
				}
				if (i + 1 < n) {
					newGrid[i+1][0] = grid[i][m-1];
				}
			}
			newGrid[0][0] = grid[n-1][m-1];
			grid = newGrid;
		}
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        for (int num = 0; num < n; num++) {
        	int[] temp = grid[num];
        	List<Integer> tempList = new ArrayList<Integer>();
        	for (int l = 0; l < m; l++) {
        		tempList.add(temp[l]);
        	}
            list.add(tempList);
        }
        return list;
    }
	
	/**
	 * 1266
	 * Date:2019-11-26
	 */
	public int minTimeToVisitAllPoints(int[][] points) {
        int result = 0;
		int length = points.length;
		for (int i = 0; i < length; i++) {
			int j = i + 1;
			if (j >= length) {
				continue;
			}
			int temp1 = points[i][0] - points[j][0];
			if (temp1 < 0) {
				temp1 = -temp1;
			}
			int temp2 = points[i][1] - points[j][1];
			if (temp2 < 0) {
				temp2 = -temp2;
			}
			result += temp1 > temp2 ? temp1:temp2;
		}
        return result;
    }

}
