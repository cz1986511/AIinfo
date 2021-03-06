package xiaozhuo.info.service.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.RemindTaskInfo;
import xiaozhuo.info.persist.base.TemplateInfo;
import xiaozhuo.info.persist.mapper.RemindTaskInfoMapper;
import xiaozhuo.info.persist.mapper.TemplateInfoMapper;

@Component
@Configurable
@EnableScheduling
@Service
@Slf4j
public class SendMessage {

	@Autowired
	private RemindTaskInfoMapper remindTaskInfoMapper;
	@Autowired
	private TemplateInfoMapper templateInfoMapper;

	@Scheduled(cron = "0 27 7 * * ?")
	public void sendAnniversary() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("remindTaskType", 1);
			List<RemindTaskInfo> remindTasks = remindTaskInfoMapper.selectRemindTasksByParams(map);
			if (!CollectionUtils.isEmpty(remindTasks)) {
				remindTasks.forEach(remindTaskInfo -> {
					LocalDate localDate = LocalDate.now();
					LocalDate remindContentDate = remindTaskInfo.getRemindContentTime();
					if (localDate.getMonthValue() == remindContentDate.getMonthValue()
							&& localDate.getDayOfMonth() == remindContentDate.getDayOfMonth()) {
						TemplateInfo templateInfo = templateInfoMapper
								.selectByPrimaryKey(remindTaskInfo.getTemplateId());
						if (null != templateInfo) {
							int number = localDate.getYear() - remindContentDate.getYear();
							String[] params = { remindTaskInfo.getRemindPerson(), number + "" };
							MailUtil.sendMail(remindTaskInfo.getRemindFrom(), remindTaskInfo.getRemindFromPass(),
									remindTaskInfo.getRemindPersonEmail(), templateInfo.getTemplateContent(),
									templateInfo.getTemplateTitle(), params);
						}
					}

				});
			}
		} catch (Exception e) {
			log.error("sendAnniversary is Exception:{}", e.toString());
		}
	}

	public static void main(String[] args) {
		if (LimitUtil.getRate()) {
			LocalDate localDate = LocalDate.now();
			System.out.println(localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth());
		}
	}

}
