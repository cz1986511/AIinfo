package xiaozhuo.info.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringTest {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String str = in.next();
		StringHandle1(str);
	}
	
	/**
	 * input:  string=aaabbbaccc
	 * output: a3b3ac3
	*/
	public static void StringHandle1(String str) {
		if (null != str && str.length() > 0) {
			char[] strArray = str.toCharArray();
			char temp;
			StringBuilder builder = new StringBuilder();
			List<Character> list = new ArrayList<Character>();
			for (int i = 0; i < strArray.length; i++) {
				temp = strArray[i];
				if(list.size() > 0) {
					int size = list.size();
					char last = list.get(size -1);
					if (temp == last) {
						list.add(temp);
					} else {
						builder.append(last);
						if (size > 1) {
							builder.append(size);
						}
						list.clear();
						list.add(temp);
					}
				} else {
					list.add(temp);
				}
			}
			char last1 = list.get(0);
			builder.append(last1);
			if (list.size() > 1) {
				builder.append(list.size());
			}
			System.out.println(builder.toString());
		}
	}

}
