package xiaozhuo.info.service;

import xiaozhuo.info.persist.base.ConsumeAmount;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhuo
 * @date   2020-08-11
 */
public interface ConsumeAmountService {

    /**
     * 保存记录
     * @param amount
     * @return
     */
    int saveConsumeAmount(ConsumeAmount amount);

    /**
     * 获取记录列表
     * @param map
     * @return
     */
    List<ConsumeAmount> getAmountList(Map<String, Object> map);

    /**
     * 获取数据汇总
     * @return
     */
    Map<String, Object> getAmountData(Map<String, Object> map);
}
