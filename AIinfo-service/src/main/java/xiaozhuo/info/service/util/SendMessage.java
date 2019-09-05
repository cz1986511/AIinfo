package xiaozhuo.info.service.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SendMessage {
	
	private static Logger logger = LoggerFactory.getLogger(SendMessage.class);
	
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
			if(!CollectionUtils.isEmpty(remindTasks)) {
				remindTasks.forEach(remindTaskInfo ->{
					LocalDate localDate = LocalDate.now();
					LocalDate remindContentDate = remindTaskInfo.getRemindContentTime();
					if(localDate.getMonthValue() == remindContentDate.getMonthValue() && localDate.getDayOfMonth() == remindContentDate.getDayOfMonth()) {
						TemplateInfo templateInfo = templateInfoMapper.selectByPrimaryKey(remindTaskInfo.getTemplateId());
						if (null != templateInfo) {
							int number = localDate.getYear() - remindContentDate.getYear();
							String[] params = {remindTaskInfo.getRemindPerson(), number+""};
							MailUtil.sendMail(remindTaskInfo.getRemindFrom(), remindTaskInfo.getRemindFromPass(), remindTaskInfo.getRemindPersonEmail(),
									templateInfo.getTemplateContent(), templateInfo.getTemplateTitle(), params);
						}
					}
					
				});
			}
		} catch(Exception e) {
			logger.error("sendAnniversary is Exception:" + e.toString());
		}
	}
	
	public static void main(String[] args) {
		LocalDate localDate = LocalDate.now();
		System.out.println(localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth());
		String s = "123456";
		String md5s = MD5Util.md5(s);
		System.out.println(md5s);
		System.out.println(MD5Util.md5(md5s));
		String[] params = {"陈卓", "3"};
		String content = "亲爱的%s,今天是你结婚%s周年纪念日";
	}

}
