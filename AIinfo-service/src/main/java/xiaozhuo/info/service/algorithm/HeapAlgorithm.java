package xiaozhuo.info.service.algorithm;

import java.util.Random;

public class HeapAlgorithm {
	
	public static int N = 1000;
	public static int LEN = 100000000;
	public static int arrs[] = new int[LEN];
	
	private static int parent(int n) {
		return (n - 1) / 2;
	}
	
	private static int left(int n) {
		return 2 * n + 1;
	}
	
	private static int right(int n) {
		return 2 * n +2;
	}
	
	//构建堆
	private static void buildHeap(int n, int[] data) {
		for(int i = 1; i < n; i++) {
			int t = i;
			while(t != 0 && data[parent(t)] > data[t]) {
				int temp = data[parent(t)];
				data[parent(t)] = data[t];
				data[t] = temp;
				t = parent(t);
			}
		}
	}
	
	//调整堆为小顶堆
	private static void adjust(int i, int n, int[] data) {
		if(data[0] >= data[i]) {
			return;
		}
		int temp = data[i];
		data[i] = data[0];
		data[0] = temp;
		int t = 0;
		//调整是,堆顶比子节点大,从较小的子节点开始调整
		while((left(t) < n && data[t] > data[left(t)]) || (right(t) < n && data[t] > data[right(t)])) {
			if(right(t) < n && data[right(t)] < data[left(t)]) {
				swap(data, t, right(t));
				t = right(t);
			} else {
				swap(data, t, left(t));
				t = left(t);
			}
		}
	}
	
	//寻找TopN
	private static void findTopN(int n, int[] data) {
		buildHeap(n, data);
		for(int i = n; i < data.length; i++) {
			adjust(i, n, data);
		}
	}
	
	private static void swap(int[] data,int i, int j) {
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < LEN; i++) {
			arrs[i] = new Random().nextInt(99999999);
			
		}
		long start = System.currentTimeMillis();
		findTopN(N, arrs);
		System.out.println(LEN + "个数,求Top " + N +",耗时:" + (System.currentTimeMillis() - start));
		for(int i = 0;i < N; i++) {
			System.out.print(arrs[i] + ",");
		}
	}

}
