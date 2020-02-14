package xiaozhuo.info.service.algorithm;

public class SortAlgorithm {
	
	public static int arrs[] = {10,4,32,5,69,1,6,42,9,3};
	
	/**
	 * 冒泡排序
	 * 时间复杂度:O(n²) O(n²) O(n)
	 * 空间复杂度:O(1)
	 * 稳定排序
	 */
	private static void maoPaoSort(int[] data) {
		int len = data.length;
		for(int i = len - 1; i > 0; i--) {
			for (int j = len - 1; j > len - i - 1; j--) {
				if (data[j] < data[j - 1]) {
					int temp = data[j];
					data[j] = data[j-1];
					data[j-1] = temp;
				}
			}
		}
	}
	
	/**
	 * 选择排序
	 * 时间复杂度:O(n²) O(n²) O(n²)
	 * 空间复杂度:O(1)
	 * 不稳定排序
	 */
	private static void selectSort(int[] data) {
		int len = data.length;
		for(int i = 0; i < len - 1; i++) {
			for (int j = i + 1; j < len; j++) {
				if (data[i] > data[j]) {
					int temp = data[i];
					data[i] = data[j];
					data[j] = temp;
				}
			}
		}
	}
	
	/**
	 * 插入排序
	 * 时间复杂度:O(n²) O(n²) O(n)
	 * 空间复杂度:O(1)
	 * 稳定排序
	 */
	private static void insertSort(int[] data) {
		int len = data.length;
		int current;
		for (int i = 0; i < len - 1; i++) {
			current = data[i+1];
			int preIndex = i;
			while (preIndex >=0 && current < data[preIndex]) {
				data[preIndex + 1] = data[preIndex];
				preIndex--;
			}
			data[preIndex + 1] = current;
		}
	}
	
	
	public static void main(String[] args) {
		maoPaoSort(arrs);
		selectSort(arrs);
		insertSort(arrs);
		for (int i:arrs) {
			System.out.print(i + ",");
		}
	}

}
