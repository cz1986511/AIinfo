package xiaozhuo.info.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaozhuo.info.persist.base.ConsumeAmount;
import xiaozhuo.info.persist.mapper.ConsumeAmountMapper;
import xiaozhuo.info.service.ConsumeAmountService;

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
}
