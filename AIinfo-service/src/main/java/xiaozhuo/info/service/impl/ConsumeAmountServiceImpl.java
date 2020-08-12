package xiaozhuo.info.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaozhuo.info.persist.base.ConsumeAmount;
import xiaozhuo.info.persist.mapper.ConsumeAmountMapper;
import xiaozhuo.info.service.ConsumeAmountService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhuo
 * @date   2020-08-11
 */
@Service
@Slf4j
public class ConsumeAmountServiceImpl implements ConsumeAmountService {

    @Autowired
    private ConsumeAmountMapper consumeAmountMapper;

    @Override
    public int saveConsumeAmount(ConsumeAmount amount) {
        if(null != amount) {
            return consumeAmountMapper.insertSelective(amount);
        }
        return 0;
    }

    @Override
    public List<ConsumeAmount> getAmountList(Map<String, Object> map) {
        return null;
    }

    @Override
    public Map<String, Object> getAmountData(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            LocalDate date = LocalDate.now();
            map.put("year", date.getYear());
            resultMap.put("yearData", consumeAmountMapper.selectAmountData(map));
            map.put("month", date.getMonthValue());
            resultMap.put("monthData", consumeAmountMapper.selectAmountData(map));
            map.put("day", date.getDayOfMonth());
            resultMap.put("dayData", consumeAmountMapper.selectAmountData(map));
        } catch(Exception e) {
            log.error("getAmountData is exception:{}", e.getStackTrace());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> searchAmountData(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            LocalDate date = LocalDate.now();
            Integer year = date.getYear();
            Integer month = date.getMonthValue();
            Integer day = date.getDayOfMonth();

            // 获取每年消费数据
            List<Integer> yearNum = Lists.newArrayList();
            List<BigDecimal> yearData = Lists.newArrayList();
            makeAmountData(map, yearNum, yearData, 2020, year, 1);
            resultMap.put("yearNum", yearNum);
            resultMap.put("yearData", yearData);

            // 获取当年每月消费数据
            List<Integer> monthNum = Lists.newArrayList();
            List<BigDecimal> monthData = Lists.newArrayList();
            makeAmountData(map, monthNum, monthData, 1, month, 2);
            resultMap.put("monthNum", monthNum);
            resultMap.put("monthData", monthData);

            // 获取当年当月每日消费数据
            List<Integer> dayNum = Lists.newArrayList();
            List<BigDecimal> dayData = Lists.newArrayList();
            makeAmountData(map, dayNum, dayData, 1, day, 3);
            resultMap.put("dayNum", dayNum);
            resultMap.put("dayData", dayData);

        } catch(Exception e) {
            log.error("getAmountData is exception:{}", e.getStackTrace());
        }
        return resultMap;
    }

    private void makeAmountData(Map<String, Object> map, List<Integer> num, List<BigDecimal> data, int start, int end, int type) {
        for(int y = start; y <= end; y++) {
            if(type == 1) {
                map.put("year", y);
            }
            if(type == 2) {
                map.put("month", y);
            }
            if(type == 3) {
                map.put("day", y);
            }
            num.add(y);
            Long yData = consumeAmountMapper.selectAmountData(map);
            if(null != yData) {
                BigDecimal temp = new BigDecimal(yData);
                data.add(temp.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            } else {
                data.add(new BigDecimal(0));
            }
        }
    }

}
