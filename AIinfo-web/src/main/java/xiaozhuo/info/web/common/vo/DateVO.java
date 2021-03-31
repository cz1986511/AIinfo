package xiaozhuo.info.web.common.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhuochen
 */
@Data
public class DateVO {

    private String dateInfo;

    private String lunar;

    private String lunarFestival;

    private String lunarSolar;

    private String calendarFestival;

    public static void main(String[] args) {
        //createExcel();
        for(int i = 1; i < 6; i++) {
            Map<Integer, Integer> mapH = new HashMap<>();
            Map<Integer, Integer> mapL = new HashMap<>();
            long time = System.currentTimeMillis();
            int result = 0;
            int lnum = 0;
            for(long k = 0; k < 88888888L; k++) {
                int count = 0;
                while(count < 6) {
                    result = (int) (Math.random() * 32) + 1;
                    if(null != mapH.get(result)) {
                        mapH.put(result, mapH.get(result) + 1);
                    } else {
                        mapH.put(result, 1);
                    }
                    count++;
                }
                lnum = (int)(Math.random() * 15) + 1;
                if(null != mapL.get(lnum)) {
                    mapL.put(lnum, mapL.get(lnum) + 1);
                } else {
                    mapL.put(lnum, 1);
                }
            }
            Map<Integer, Integer> sortMapH = new LinkedHashMap<Integer, Integer>();
            mapH.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMapH.put(x.getKey(), x.getValue()));

            Map<Integer, Integer> sortMapL = new LinkedHashMap<Integer, Integer>();
            mapL.entrySet().stream().sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed()).forEachOrdered(x -> sortMapL.put(x.getKey(), x.getValue()));

            System.out.println("第" + i + "次:");
            System.out.println("time:" + (System.currentTimeMillis()-time));

            Iterator<Map.Entry<Integer, Integer>> iterator = sortMapH.entrySet().iterator();
            int countH = 0;
            System.out.print("红球:");
            while(iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = iterator.next();
                System.out.print(entry.getKey() + "=" + entry.getValue() + ",");
                countH++;
                if(countH == 6) {
                    break;
                }
            }
            System.out.println();
            Iterator<Map.Entry<Integer, Integer>> iteratorL = sortMapL.entrySet().iterator();
            int countL = 0;
            System.out.print("蓝球:");
            while(iteratorL.hasNext()) {
                Map.Entry<Integer, Integer> entryL = iteratorL.next();
                System.out.println(entryL.getKey() + "=" + entryL.getValue());
                countL++;
                if(countL == 1) {
                    break;
                }
            }
        }
    }


}
