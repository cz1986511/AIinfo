package com.danlu.dleye.manager.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.constants.ManagerType;
import com.danlu.dleye.manager.DleyeManager;
import com.danlu.dleye.util.CalendarUtil;
import com.danlu.dleye.util.ManagerLoggerUtil;

public class QuartzManager implements DleyeManager {

    private static final Logger log = Logger.getLogger(QuartzManager.class);

    private PrintStream info;

    private Map<String, Scheduler> schedulerFactories;

    private Map<String, Object> beans;

    private Lock lock;

    private static Map<String, Map<String, Boolean>> manualTriggers = new HashMap<String, Map<String, Boolean>>();

    private final TreeSet<File> fileSet;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public QuartzManager() {
        this.info = null;

        this.schedulerFactories = new LinkedHashMap();

        this.beans = new HashMap();

        this.lock = new ReentrantLock();

        this.fileSet = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                return f1.getName().compareTo(f2.getName()) * -1;
            }
        });
    }

    @Override
    public String service(Map<String, String> queryParams) {
        String action = (String) queryParams.get("action");
        String jobName = "";
        String jobGroup = "";
        String fname = "";
        String newCronExp = "";
        Boolean isSetSuccess = null;
        StringBuilder result = new StringBuilder();
        if (StringUtils.isBlank(action)) {
            action = "list";
        }
        if (action.equals("list")) {
            return listCrons();
        }
        if (action.equals("invoke")) {
            jobName = (String) queryParams.get("jobName");
            jobGroup = (String) queryParams.get("jobGroup");
            fname = (String) queryParams.get("fname");
            if ((StringUtils.isBlank(jobGroup)) || (StringUtils.isBlank(jobName)) || (StringUtils.isBlank(fname))) {
                log.info("missing jobGroup or jobName");
                return listCrons();
            }
            return invoke(jobGroup, jobName, fname);
        }
        if (action.equals("pause")) {
            jobGroup = (String) queryParams.get("jobGroup");
            jobName = (String) queryParams.get("jobName");
            fname = (String) queryParams.get("fname");
            if ((StringUtils.isBlank(jobGroup)) || (StringUtils.isBlank(jobName)) || (StringUtils.isBlank(fname))) {
                log.info("missing jobGroup or jobName");
                return listCrons();
            }
            return pauseJob(jobGroup, jobName, fname);
        }
        if (action.equals("resume")) {
            jobGroup = (String) queryParams.get("jobGroup");
            jobName = (String) queryParams.get("jobName");
            fname = (String) queryParams.get("fname");
            if ((StringUtils.isBlank(jobGroup)) || (StringUtils.isBlank(jobName)) || (StringUtils.isBlank(fname))) {
                log.info("missing jobGroup or jobName");
                return listCrons();
            }
            return resumeJob(jobGroup, jobName, fname);
        }
        if (action.equals("setNewCronExp")) {
            jobGroup = (String) queryParams.get("jobGroup");
            jobName = (String) queryParams.get("jobName");
            newCronExp = (String) queryParams.get("newCronExp");
            fname = (String) queryParams.get("fname");

            if ((StringUtils.isBlank(jobGroup)) || (StringUtils.isBlank(jobName)) || (StringUtils.isBlank(newCronExp))
                    || (StringUtils.isBlank(fname))) {
                log.info("missing jobGroup or jobName");
                return listCrons();
            }
            try {
                newCronExp = URIUtil.decode(newCronExp);
            } catch (URIException e) {
                log.info("CronExp error ");
                return listCrons();
            }
            isSetSuccess = setNewCronExp(jobGroup, jobName, newCronExp, fname);
            if ((isSetSuccess != null) && (isSetSuccess.booleanValue()))
                result.append("true");
            else if ((isSetSuccess != null) && (!isSetSuccess.booleanValue())) {
                result.append("false");
            }
            return result.toString();
        }
        if (action.equals("start")) {
            jobGroup = (String) queryParams.get("jobGroup");
            jobName = (String) queryParams.get("jobName");
            fname = (String) queryParams.get("fname");
            if (StringUtils.isBlank(fname)) {
                log.info("missing scheduler");
                return listCrons();
            }
            return start(fname);
        }
        if (action.equals("stop")) {
            fname = (String) queryParams.get("fname");
            if (StringUtils.isBlank(fname)) {
                log.info("missing scheduler");
                return listCrons();
            }
            return stop(fname);
        }
        if (action.equals("setPersist")) {
            jobGroup = (String) queryParams.get("jobGroup");
            jobName = (String) queryParams.get("jobName");
            fname = (String) queryParams.get("fname");
            result = new StringBuilder();
            try {
                result.append(persist(jobGroup, jobName, fname));
            } catch (Exception e) {
                result.append(new StringBuilder().append(" persist falied exception ").append(e).toString());
            }
            return result.toString();
        }
        return listCrons();
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void init(ServletContext paramServletContext, PrintStream initLogger, Map<String, Object> beans) {
        this.info = initLogger;
        this.beans = beans;

        for (Map.Entry entry : beans.entrySet()) {
            String beanName = (String) entry.getKey();
            Object bean = entry.getValue();
            if ((bean instanceof Scheduler)) {
                this.schedulerFactories.put(beanName, (Scheduler) bean);
            }
        }
        try {
            makeThePersistFileTakeEffect();
        } catch (Exception e) {
            this.info.println(new StringBuilder().append("Taking quartz effect exception,").append(ManagerLoggerUtil.convertThrowableToString(e))
                    .toString());
        }
        this.info.println(new StringBuilder().append("There are ").append(this.schedulerFactories.size()).append(" scheduler(s).").toString());

    }

    @Override
    public ManagerType getType() {
        return ManagerType.QUARTZ;
    }

    private String listCrons() {
        JSONArray _ret = new JSONArray();
        Set<String> keySet = this.schedulerFactories.keySet();
        for (String name : keySet) {
            
            Scheduler scheduler = (Scheduler) this.schedulerFactories.get(name);
            try {
                GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
                Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
                if (!CollectionUtils.isEmpty(jobKeys)) {
                    Iterator<JobKey> ite = jobKeys.iterator();
                    while (ite.hasNext()) {
                    	JSONObject ret = new JSONObject();
                        ret.element("factory", name);
                        ret.element("isStarted", scheduler.isStarted());
                        ret.element("isStandby", scheduler.isInStandbyMode());
                        JobKey jobKey = ite.next();
                        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                        for (Trigger trigger : triggers) {
                            if ((trigger instanceof CronTrigger)) {
                                CronTrigger tb = (CronTrigger) trigger;
                                Map<String, Object> miMap = new HashMap<String, Object>();
                                miMap.put("jobName", trigger.getJobKey().getName());
                                miMap.put("jobGroup", trigger.getJobKey().getGroup());
                                miMap.put("desc", trigger.getDescription());
                                miMap.put("cronExp", tb.getCronExpression());
                                try {
                                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                                    miMap.put("state", triggerState.name());
                                } catch (SchedulerException e) {
                                    log.error("GetTriggerStateError", e);
                                }
                                ret.element("job", miMap);
                            }
                        }
                        _ret.element(ret);
                    }
                }
            } catch (SchedulerException e) {
                log.error("SchedulerError", e);
            }
        }
        log.debug(new StringBuilder().append("SchedulerFactoryBean  list is : ").append(_ret.toString()).toString());
        return _ret.toString();
    }

    private String pauseJob(String jobGroup, String jobName, String fname) {
        Boolean isPause = Boolean.valueOf(false);
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
            log.info(new StringBuilder().append("Pause Job ").append(jobName).append(":").append(jobGroup).toString());
        } catch (SchedulerException e) {
            log.info(new StringBuilder().append("Pause Job Error:").append(jobName).append(":").append(jobGroup).toString(), e);
            return isPause.toString();
        }
        isPause = Boolean.valueOf(true);
        return isPause.toString();
    }

    private String resumeJob(String jobGroup, String jobName, String fname) {
        Boolean isResune = Boolean.valueOf(false);
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
            log.info(new StringBuilder().append("Resume Job ").append(jobName).append(":").append(jobGroup).toString());
        } catch (SchedulerException e) {
            log.info(new StringBuilder().append("Resume Job Error:").append(jobName).append(":").append(jobGroup).toString(), e);

            return isResune.toString();
        }
        isResune = Boolean.valueOf(true);
        return isResune.toString();
    }

    private Boolean setNewCronExp(String jobGroup, String jobName, String newCronExp, String fname) {
        Boolean isNewCron = Boolean.valueOf(false);
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(newCronExp);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info(new StringBuilder().append("Change Job CronExpression of ").append(jobName).append(":").append(jobGroup).append(" to:")
                    .append(newCronExp).toString());
        } catch (SchedulerException e) {
            log.error(new StringBuilder().append("ChangeJobCronExpressionError:").append(jobGroup).append(".").append(jobName).toString(), e);
            return isNewCron;
        }
        isNewCron = Boolean.valueOf(true);
        return isNewCron;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String invoke(String jobGroup, String jobName, String fname) {
        String k = new StringBuilder().append(jobGroup).append(".").append(jobName).toString();
        Boolean isInvoke = Boolean.valueOf(false);
        if (!manualTriggers.containsKey(k)) {
            manualTriggers.put(k, new TreeMap());
        }
        String now = CalendarUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);

            log.info(new StringBuilder().append("Trigger Job:").append(jobName).append(":").append(jobGroup).toString());
        } catch (SchedulerException e) {
            ((Map) manualTriggers.get(k)).put(now, Boolean.valueOf(false));
            log.error(new StringBuilder().append("JobException:").append(jobGroup).append(".").append(jobName).toString(), e);
            return isInvoke.toString();
        }
        ((Map) manualTriggers.get(k)).put(now, Boolean.valueOf(true));
        isInvoke = Boolean.valueOf(true);
        return isInvoke.toString();
    }

    public String start(String fname) {
        Boolean isStart = Boolean.valueOf(false);
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);
            scheduler.start();
            isStart = Boolean.valueOf(true);
        } catch (SchedulerException e) {
            log.error(new StringBuilder().append("SchedulerException:").append(fname).toString(), e);
            return isStart.toString();
        }
        return isStart.toString();
    }

    public String stop(String fname) {
        Boolean isStop = Boolean.valueOf(false);
        try {
            Scheduler scheduler = (Scheduler) this.beans.get(fname);
            scheduler.standby();
            isStop = Boolean.valueOf(true);
        } catch (SchedulerException e) {
            log.error(new StringBuilder().append("SchedulerException:").append(fname).toString(), e);
            return isStop.toString();
        }
        return isStop.toString();
    }

    public String persist(String triggerGroup, String triggerName, String fname) throws Exception {
        this.lock.lock();
        try {
            Map<String, QuartzInfo> persistInfo = new HashMap<String, QuartzInfo>();
            File path = new File("/home/admin/.ateye/quartz/");
            if (!path.exists()) {
                if (path.mkdirs() != true) {
                    throw new RuntimeException("Failed to create the Path: /home/admin/.dleye/quartz/");
                }
            }
            File[] persistFiles = path.listFiles();
            if ((persistFiles != null) && (persistFiles.length > 0)) {
                File oldFile = getTheLatestFile(persistFiles);
                persistInfo = populateWithPersistFile(oldFile);

                String oldFileName = oldFile.getName();
                String oldNumberStr = oldFileName.substring("AteyeQuartzPersist_".length());
                int oldNumber = Integer.parseInt(oldNumberStr);
                String newFileName = new StringBuilder().append("AteyeQuartzPersist_").append(++oldNumber).toString();
                saveInfoToNewFile(triggerGroup, triggerName, fname, persistInfo, new File(path, newFileName));
                for (File f : persistFiles) {
                    f.delete();
                }
            } else {
                String newFileName = "AteyeQuartzPersist_0";
                saveInfoToNewFile(triggerGroup, triggerName, fname, persistInfo, new File(path, newFileName));
            }
        } finally {
            this.lock.unlock();
        }
        return "persist is successful";
    }

    private File getTheLatestFile(File[] files) {
        if (files != null) {
            this.fileSet.clear();
            Collections.addAll(this.fileSet, files);
            return (File) this.fileSet.iterator().next();
        }
        return null;
    }

    @SuppressWarnings("resource")
    private Map<String, QuartzInfo> populateWithPersistFile(File persistFile) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(persistFile));
        String line = null;
        HashMap<String, QuartzInfo> persistInfo = new HashMap<String, QuartzInfo>();
        while ((line = in.readLine()) != null) {
            int triggerGroupIndex = line.indexOf("TriggerGroup");
            int triggerNameIndex = line.indexOf("TriggerName");
            int newCronExpIndex = line.indexOf("NewCronExp");
            int fnameIndex = line.indexOf("Fname");
            int schedulerStartedIndex = line.indexOf("SchedulerStarted");
            int triggerPausedIndex = line.indexOf("TriggerPaused");
            if ((triggerGroupIndex < 0) || (triggerNameIndex < 0) || (newCronExpIndex < 0) || (fnameIndex < 0) || (schedulerStartedIndex < 0)
                    || (triggerPausedIndex < 0)) {
                throw new RuntimeException(new StringBuilder().append("The persist file's content is not valid, line:").append(line).toString());
            }

            String persistTriggerGroup = line.substring(13, triggerNameIndex - 1);
            String persistTriggerName = line.substring(triggerNameIndex + 12, newCronExpIndex - 1);
            String persistNewCronExp = line.substring(newCronExpIndex + 11, fnameIndex - 1);
            String persistFname = line.substring(fnameIndex + 6, schedulerStartedIndex - 1);
            String schedulerStarted = line.substring(schedulerStartedIndex + 17, triggerPausedIndex - 1);
            String triggerPaused = line.substring(triggerPausedIndex + 14);
            QuartzInfo quartzInfo = new QuartzInfo();
            quartzInfo.setTriggerGroup(persistTriggerGroup);
            quartzInfo.setNewCronExp(persistNewCronExp);
            quartzInfo.setFname(persistFname);
            quartzInfo.setTriggerName(persistTriggerName);
            if ((schedulerStarted != null) && (schedulerStarted.equals("true")))
                quartzInfo.setSchedulerStarted(Boolean.valueOf(true));
            else {
                quartzInfo.setSchedulerStarted(Boolean.valueOf(false));
            }
            if ((triggerPaused != null) && (triggerPaused.equals("true")))
                quartzInfo.setTriggerPaused(Boolean.valueOf(true));
            else {
                quartzInfo.setTriggerPaused(Boolean.valueOf(false));
            }
            persistInfo.put(new StringBuilder().append(persistFname).append(":").append(persistTriggerName).toString(), quartzInfo);
        }

        in.close();
        return persistInfo;
    }

    @SuppressWarnings({ "rawtypes" })
    private void saveInfoToNewFile(String jobGroup, String jobName, String fname, Map<String, QuartzInfo> persistInfo, File newFile) throws Exception {
        Scheduler scheduler = (Scheduler) this.beans.get(fname);
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        CronTriggerBean cronTriggerBean = (CronTriggerBean) trigger;

        String newExp = cronTriggerBean.getCronExpression();
        QuartzInfo newInfo = new QuartzInfo();
        newInfo.setTriggerGroup(jobGroup);
        newInfo.setFname(fname);
        newInfo.setTriggerName(jobName);
        newInfo.setNewCronExp(newExp);

        if (scheduler.isInStandbyMode())
            newInfo.setSchedulerStarted(Boolean.valueOf(false));
        else {
            newInfo.setSchedulerStarted(Boolean.valueOf(true));
        }

        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

        if (triggerState.name().equals("PAUSED")) {
            newInfo.setTriggerPaused(Boolean.valueOf(true));
        } else {
            newInfo.setTriggerPaused(Boolean.valueOf(false));
        }

        persistInfo.put(new StringBuilder().append(fname).append(":").append(jobName).toString(), newInfo);
        PrintWriter out = new PrintWriter(newFile);
        for (Map.Entry entry : persistInfo.entrySet()) {
            out.println(new StringBuilder().append("JonGroup:").append(((QuartzInfo) entry.getValue()).getTriggerGroup()).append(" JobName:")
                    .append(((QuartzInfo) entry.getValue()).getTriggerName()).append(" NewCronExp:")
                    .append(((QuartzInfo) entry.getValue()).getNewCronExp()).append(" Fname:").append(((QuartzInfo) entry.getValue()).getFname())
                    .append(" SchedulerStarted:").append(((QuartzInfo) entry.getValue()).getSchedulerStarted()).append(" JobPaused:")
                    .append(((QuartzInfo) entry.getValue()).getTriggerPaused()).toString());
        }

        out.close();
    }

    @SuppressWarnings({ "rawtypes" })
    private void makeThePersistFileTakeEffect() throws Exception {
        File path = new File("/home/admin/.dleye/quartz/");
        if (path.exists()) {
            File[] persistFiles = path.listFiles();
            if ((persistFiles != null) && (persistFiles.length > 0)) {
                File oldFile = getTheLatestFile(persistFiles);
                Map<String, QuartzInfo> persistInfo = populateWithPersistFile(oldFile);

                for (Map.Entry entry : persistInfo.entrySet()) {
                    this.info.println("About to make a persist quartz take effect");
                    QuartzInfo quartzInfo = (QuartzInfo) entry.getValue();
                    if (quartzInfo == null) {
                        return;
                    }
                    String triggerName = quartzInfo.getTriggerName();
                    String triggerGroup = quartzInfo.getTriggerGroup();
                    String newCronExp = quartzInfo.getNewCronExp();
                    String fname = quartzInfo.getFname();
                    Boolean isStarted = null;
                    Boolean isPaused = null;
                    boolean isEffected = false;
                    isStarted = quartzInfo.getSchedulerStarted();
                    isPaused = quartzInfo.getTriggerPaused();
                    isEffected = (StringUtils.isNotBlank(triggerName)) && (StringUtils.isNotBlank(triggerGroup))
                            && (StringUtils.isNotBlank(newCronExp)) && (StringUtils.isNotBlank(fname)) && (isStarted != null) && (isPaused != null);

                    if (isEffected) {
                        try {
                            setNewCronExp(triggerGroup, triggerName, newCronExp, fname);
                        } catch (Exception e) {
                            this.info.println(new StringBuilder().append("Invalid triggerGroup:").append(triggerGroup).append(",triggerName:")
                                    .append(triggerName).append(",newCronExp").append(newCronExp).append(",fname").append(fname).toString());
                            this.info.println(ManagerLoggerUtil.convertThrowableToString(e));
                        }
                        if (isPaused.booleanValue()) {
                            pauseJob(triggerGroup, triggerName, fname);
                        } else {
                            resumeJob(triggerGroup, triggerName, fname);
                        }
                        if (isStarted.booleanValue()) {
                            start(fname);
                        } else {
                            stop(fname);
                        }
                    } else {
                        this.info.println(new StringBuilder().append("Invalid triggerGroup:").append(triggerGroup).append(",triggerName:")
                                .append(triggerName).append(",newCronExp").append(newCronExp).append(",fname").append(fname).toString());
                    }
                }
            }
        }
    }

    private static class QuartzInfo {
        private String jobGroup;

        private String jobName;

        private String newCronExp;

        private String fname;

        private Boolean jobPaused;

        private Boolean schedulerStarted;

        public String getTriggerGroup() {
            return this.jobGroup;
        }

        public void setTriggerGroup(String jobGroup) {
            this.jobGroup = jobGroup;
        }

        public String getTriggerName() {
            return this.jobName;
        }

        public void setTriggerName(String jobName) {
            this.jobName = jobName;
        }

        public String getNewCronExp() {
            return this.newCronExp;
        }

        public void setNewCronExp(String newCronExp) {
            this.newCronExp = newCronExp;
        }

        public String getFname() {
            return this.fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public Boolean getTriggerPaused() {
            return this.jobPaused;
        }

        public void setTriggerPaused(Boolean jobPaused) {
            this.jobPaused = jobPaused;
        }

        public Boolean getSchedulerStarted() {
            return this.schedulerStarted;
        }

        public void setSchedulerStarted(Boolean schedulerStarted) {
            this.schedulerStarted = schedulerStarted;
        }
    }

}
