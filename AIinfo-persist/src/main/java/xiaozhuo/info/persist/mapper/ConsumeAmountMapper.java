package xiaozhuo.info.persist.mapper;

import org.apache.ibatis.annotations.Param;
import xiaozhuo.info.persist.base.ConsumeAmount;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhuo
 * @date   2020-08-11
 */
public interface ConsumeAmountMapper {
    /**
     * 根据主键删除数据
     * @param id
     * @return
     */
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * 新增数据
     * @param record
     * @return
     */
    int insertSelective(ConsumeAmount record);

    /**
     * 主键查询
     * @param id
     * @return
     */
    ConsumeAmount selectByPrimaryKey(Long id);

    /**
     * 更新数据
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ConsumeAmount record);

    /**
     * 通过条件查询
     * @param map
     * @return
     */
    List<ConsumeAmount> selectAmountsByParams(Map<String, Object> map);

    /**
     * 通过条件查询统计数据
     * @param map
     * @return
     */
    Long selectAmountData(Map<String, Object> map);

}