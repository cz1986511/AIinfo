package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.RemindTaskInfo;

public interface RemindTaskInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RemindTaskInfo remindTaskInfo);

    int insertSelective(RemindTaskInfo remindTaskInfo);

    RemindTaskInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RemindTaskInfo remindTaskInfo);

    int updateByPrimaryKey(RemindTaskInfo remindTaskInfo);
    
    List<RemindTaskInfo> selectRemindTasksByParams(Map<String, Object> map);
}