package com.danlu.dleye.manager.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.annotation.Switch;
import com.danlu.dleye.constants.ManagerType;
import com.danlu.dleye.manager.DleyeManager;
import com.danlu.dleye.util.ManagerLoggerUtil;

public class SwitchManager implements DleyeManager {

    private PrintStream info;

    private Map<Class<?>, Class<?>> typeConverter;

    private Lock lock;

    @SuppressWarnings("unused")
    private static final String _PERSIST_FILE_PATH = "/home/admin/.dleye/switch/";

    @SuppressWarnings("unused")
    private static final String _PERSIST_FILE_NAME_MAIN = "DleyeSwitchPersis_";

    private Map<String, Map<String, SwitchInfo>> switches;

    private Map<String, Object> beans;

    private final TreeSet<File> fileSet;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public SwitchManager() {
        this.info = null;
        this.typeConverter = new HashMap();

        this.typeConverter.put(Boolean.TYPE, Boolean.class);
        this.typeConverter.put(Boolean.class, Boolean.class);
        this.typeConverter.put(Byte.TYPE, Byte.class);
        this.typeConverter.put(Byte.class, Byte.class);
        this.typeConverter.put(Short.TYPE, Short.class);
        this.typeConverter.put(Short.class, Short.class);
        this.typeConverter.put(Character.TYPE, Character.class);
        this.typeConverter.put(Character.class, Character.class);
        this.typeConverter.put(Integer.TYPE, Integer.class);
        this.typeConverter.put(Integer.class, Integer.class);
        this.typeConverter.put(Long.TYPE, Long.class);
        this.typeConverter.put(Long.class, Long.class);
        this.typeConverter.put(Float.TYPE, Float.class);
        this.typeConverter.put(Float.class, Float.class);
        this.typeConverter.put(Double.TYPE, Double.class);
        this.typeConverter.put(Double.class, Double.class);

        this.typeConverter.put(String.class, String.class);
        this.typeConverter.put(Date.class, Date.class);

        this.lock = new ReentrantLock();

        this.switches = new HashMap();
        this.beans = new HashMap();

        this.fileSet = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                return f1.getName().compareTo(f2.getName()) * -1;
            }
        });
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String service(Map<String, String> queryParams) {
        String action = (String) queryParams.get("action");
        if ("list".equalsIgnoreCase(action)) {
            JSONArray array = queryAllFieldsInfo();
            return array.toString();
        }
        if ("set".equalsIgnoreCase(action)) {
            JSONObject json = new JSONObject();
            String beanName = (String) queryParams.get("beanName");
            String fieldName = (String) queryParams.get("fieldName");
            String newValueStr = (String) queryParams.get("newValue");

            String full = (String) queryParams.get("full");

            String persist = (String) queryParams.get("persist");

            JSONArray array = new JSONArray();
            if (this.switches.get(beanName) != null) {
                if (((Map) this.switches.get(beanName)).get(fieldName) != null) {
                    SwitchInfo si = (SwitchInfo) ((Map) this.switches.get(beanName)).get(fieldName);
                    try {
                        setCurrentSwitchValueOfABean(beanName, newValueStr, si);
                        json.put("SetSuccess", "true");

                        if ("true".equalsIgnoreCase(persist)) {
                            try {
                                persist(beanName, fieldName, newValueStr);
                                json.put("PersistSuccess", "true");
                            } catch (Exception pe) {
                                json.put("PersistSuccess", "false");
                                json.put("PersistException", ManagerLoggerUtil.convertThrowableToString(pe));
                            }
                        }
                    } catch (Exception se) {
                        json.put("SetSuccess", "false");
                        json.put("SetException", ManagerLoggerUtil.convertThrowableToString(se));
                    }
                } else {
                    json.put("SetSuccess", "false");
                    json.put("SetException", "Invalid FieldName:" + fieldName);
                }
            } else {
                json.put("SetSuccess", "false");
                json.put("SetException", "Invalid BeanName:" + beanName);
            }
            array.add(json);
            if ((full != null) && (!"".equals(full))) {
                JSONArray allFieldsInfo = queryAllFieldsInfo();
                array.add(allFieldsInfo);
            }
            return array.toString();
        }

        JSONObject json = new JSONObject();
        json.put("success", "false");
        json.put("errorMsg", "Invalid action type:" + action);
        return json.toString();
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void init(ServletContext paramServletContext, PrintStream paramPrintStream, Map<String, Object> paramMap) {
        this.info = paramPrintStream;
        this.beans = paramMap;

        for (Map.Entry entry : beans.entrySet()) {
            String beanName = (String) entry.getKey();
            Object bean = entry.getValue();
            Map<String, SwitchInfo> switchesOfABean = getAllSwitchesOfABean(beanName, bean);
            this.switches.put(beanName, switchesOfABean);
        }
        this.info.println("Switches size is :" + getSwitchCount());
        try {
            makeThePersistFileTakeEffect();
        } catch (Exception e) {
            this.info.println("Taking switch effect exception," + ManagerLoggerUtil.convertThrowableToString(e));
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<String, SwitchInfo> getAllSwitchesOfABean(String beanName, Object bean) {
        Map<String, SwitchInfo> switchesOfABean = new HashMap<String, SwitchInfo>();
        Class c = AopUtils.getTargetClass(bean);
        do {
            for (Field field : c.getDeclaredFields()) {
                Switch sw = (Switch) field.getAnnotation(Switch.class);
                if ((sw != null) && (this.typeConverter.containsKey(field.getType()))) {
                    String sname = null;
                    if ((sw.name() != null) && (!"".equals(sw.name()))) {
                        sname = sw.name();
                    } else {
                        sname = bean.getClass().getName() + "." + field.getName();
                    }
                    String sdesc = sw.description();
                    SwitchInfo si = new SwitchInfo();
                    si.setSname(sname);
                    si.setSdesc(sdesc);
                    si.setField(field);

                    String getMethodName = generateGetMethodName(field);
                    Method getMethod = null;
                    try {
                        getMethod = c.getDeclaredMethod(getMethodName, new Class[0]);
                    } catch (Exception e) {
                        this.info.println("Failed to obtain the get method, beanName: " + beanName + " ,Method: " + getMethodName + " ,Class: "
                                + c.getCanonicalName());
                        e.printStackTrace(this.info);
                    }
                    if (getMethod != null) {
                        si.setGetMethod(getMethod);
                    }

                    String setMethodName = generateSetMethodName(field);
                    Method setMethod = null;
                    try {
                        setMethod = c.getDeclaredMethod(setMethodName, new Class[] { field.getType() });
                    } catch (Exception e) {
                        this.info.println("Failed to obtain the set method, beanName: " + beanName + " ,Method: " + setMethodName + " ,Class: "
                                + c.getCanonicalName());
                        e.printStackTrace(this.info);
                    }
                    if (setMethod != null) {
                        si.setSetMethod(setMethod);
                    }
                    this.info.println("Finding a switch, beanName: " + beanName + " ,Class: " + c.getCanonicalName());
                    switchesOfABean.put(field.getName(), si);
                }
            }
            c = c.getSuperclass();
        } while (c != null);
        return switchesOfABean;
    }

    private String generateGetMethodName(Field field) {
        String fieldName = field.getName();
        if (field.getType() == Boolean.TYPE) {
            if ((fieldName.startsWith("is")) && (fieldName.length() > 2) && (Character.isUpperCase(fieldName.charAt(2)))) {
                return fieldName;
            }
        }
        String camelName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (field.getType() == Boolean.TYPE) {
            return "is" + camelName;
        }

        return "get" + camelName;
    }

    private String generateSetMethodName(Field field) {
        String fieldName = field.getName();
        String camelName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return "set" + camelName;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private JSONArray queryAllFieldsInfo() {
        JSONArray array = new JSONArray();
        String beanName;
        for (Map.Entry entry_out : this.switches.entrySet()) {
            beanName = (String) entry_out.getKey();
            Map<String, SwitchInfo> value = (Map<String, SwitchInfo>) entry_out.getValue();
            for (Map.Entry entry_in : value.entrySet()) {
                String fieldName = (String) entry_in.getKey();
                SwitchInfo si = (SwitchInfo) entry_in.getValue();
                JSONObject json = new JSONObject();
                json.put("beanName", beanName);
                json.put("fieldName", fieldName);
                json.put("sname", si.getSname());
                json.put("sdesc", si.getSdesc());

                String longStype = si.getField().getType().getName();
                json.put("stype", longStype.substring(longStype.lastIndexOf(".") + 1));

                Object currentValue = null;
                try {
                    currentValue = getCurrentSwitchValueOfABean(beanName, si);
                } catch (Exception e) {
                    json.put("GetException", ManagerLoggerUtil.convertThrowableToString(e));
                }
                json.put("currValue", currentValue != null ? currentValue.toString() : null);
                array.add(json);
            }
        }

        return array;
    }

    private Object getCurrentSwitchValueOfABean(String beanName, SwitchInfo si) {
        Object bean = this.beans.get(beanName);
        if (si.getGetMethod() != null) {
            Method getMethod = si.getGetMethod();
            getMethod.setAccessible(true);
            try {
                if ((bean instanceof Advised)) {
                    bean = ((Advised) bean).getTargetSource().getTarget();
                }
                return getMethod.invoke(bean, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException("利用Get方法返回开关当前值异常,BeanName:" + beanName + ",FieldName:" + si.getField().getName(), e);
            }

        }

        si.getField().setAccessible(true);
        try {
            if ((bean instanceof Advised)) {
                bean = ((Advised) bean).getTargetSource().getTarget();
            }
            return si.getField().get(bean);
        } catch (Exception e) {
            throw new RuntimeException("直接返回开关当前值异常,BeanName:" + beanName + ",FieldName:" + si.getField().getName(), e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setCurrentSwitchValueOfABean(String beanName, String newValueStr, SwitchInfo si) throws Exception {
        Object bean = this.beans.get(beanName);
        Object newValue = null;

        if (si.getSetMethod() != null) {
            try {
                Method setMethod = si.getSetMethod();
                Class parameterType = setMethod.getParameterTypes()[0];
                if (parameterType == String.class) {
                    newValue = newValueStr;
                } else {
                    parameterType = (Class) this.typeConverter.get(parameterType);
                    if (parameterType == Character.class) {
                        newValue = Character.valueOf(newValueStr.toCharArray()[0]);
                    } else if (parameterType == Date.class) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(newValueStr);
                        newValue = date;
                    } else {
                        Method transformedMethod = parameterType.getMethod("valueOf", new Class[] { String.class });
                        newValue = transformedMethod.invoke(null, new Object[] { newValueStr });
                    }
                }
                if (newValue != null) {
                    if ((bean instanceof Advised)) {
                        bean = ((Advised) bean).getTargetSource().getTarget();
                    }
                    setMethod.invoke(bean, new Object[] { newValue });
                }
            } catch (Exception e) {
                throw new RuntimeException("利用set方法设置开关当前值异常,BeanName:" + beanName + ",FieldName:" + si.getField().getName() + ",newValue:"
                        + newValueStr, e);
            }

        } else {
            try {
                Field field = si.getField();
                field.setAccessible(true);
                Class fieldClass = field.getType();
                if (fieldClass == String.class) {
                    newValue = newValueStr;
                } else {
                    fieldClass = (Class) this.typeConverter.get(fieldClass);
                    if (fieldClass == Character.class) {
                        newValue = Character.valueOf(newValueStr.toCharArray()[0]);
                    } else if (fieldClass == Date.class) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(newValueStr);
                        newValue = date;
                    } else {
                        Method transformedMethod = fieldClass.getMethod("valueOf", new Class[] { String.class });
                        newValue = transformedMethod.invoke(null, new Object[] { newValueStr });
                    }
                }
                if (newValue != null) {
                    if ((bean instanceof Advised)) {
                        bean = ((Advised) bean).getTargetSource().getTarget();
                    }
                    field.set(bean, newValue);
                }
            } catch (Exception e) {
                throw new RuntimeException("直接设置开关当前值异常,BeanName:" + beanName + ",FieldName:" + si.getField().getName() + ",newValue:" + newValueStr,
                        e);
            }
        }
    }

    @Override
    public ManagerType getType() {
        return ManagerType.SWITCH;
    }

    public void persist(String beanName, String fieldName, String newValueStr) throws Exception {
        this.lock.lock();
        try {
            Map<String, Map<String, String>> persistInfo = new HashMap<String, Map<String, String>>();
            File path = new File("/home/admin/.dleye/switch/");
            if (!path.exists()) {
                if (path.mkdirs() != true) {
                    throw new RuntimeException("Failed to create the Path: /home/admin/.dleye/switch/");
                }
            }
            File[] persistFiles = path.listFiles();
            if ((persistFiles != null) && (persistFiles.length > 0)) {
                File oldFile = getTheLatestFile(persistFiles);
                persistInfo = populateWithPersistFile(oldFile);

                String oldFileName = oldFile.getName();
                String oldNumberStr = oldFileName.substring("DleyeSwitchPersis_".length());
                int oldNumber = Integer.parseInt(oldNumberStr);
                String newFileName = "DleyeSwitchPersis_" + ++oldNumber;
                saveInfoToNewFile(beanName, fieldName, newValueStr, persistInfo, new File(path, newFileName));
                for (File f : persistFiles) {
                    f.delete();
                }
            } else {
                String newFileName = "DleyeSwitchPersis_0";
                saveInfoToNewFile(beanName, fieldName, newValueStr, persistInfo, new File(path, newFileName));
            }
        } finally {
            this.lock.unlock();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private void saveInfoToNewFile(String beanName, String fieldName, String newValueStr, Map<String, Map<String, String>> persistInfo, File newFile)
            throws Exception {
        if (persistInfo.get(beanName) == null) {
            persistInfo.put(beanName, new HashMap<String, String>());
        }
        ((Map) persistInfo.get(beanName)).put(fieldName, newValueStr);
        PrintWriter out = new PrintWriter(newFile);
        for (Iterator i$ = persistInfo.entrySet().iterator(); i$.hasNext();) {
            Map.Entry outEntry = (Map.Entry) i$.next();

            for (Map.Entry innerEntry : ((Map<String, String>) outEntry.getValue()).entrySet()) {
                out.println("BeanName:" + (String) outEntry.getKey() + " FieldName:" + (String) innerEntry.getKey() + " SwitchValue:"
                        + (String) innerEntry.getValue());
            }
        }
        Map.Entry outEntry;
        out.close();
    }

    private File getTheLatestFile(File[] files) {
        if (files != null) {
            this.fileSet.clear();
            Collections.addAll(this.fileSet, files);
            return (File) this.fileSet.iterator().next();
        }
        return null;
    }

    private int getSwitchCount() {
        int count = 0;
        for (Map<String, SwitchInfo> switchesOfABean : this.switches.values()) {
            count += switchesOfABean.size();
        }
        return count;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private void makeThePersistFileTakeEffect() throws Exception {
        File path = new File("/home/admin/.dleye/switch/");
        Iterator i$;
        if (path.exists()) {
            File[] persistFiles = path.listFiles();
            if ((persistFiles != null) && (persistFiles.length > 0)) {
                File oldFile = getTheLatestFile(persistFiles);
                Map persistInfo = populateWithPersistFile(oldFile);

                for (i$ = persistInfo.entrySet().iterator(); i$.hasNext();) {
                    Map.Entry outEntry = (Map.Entry) i$.next();

                    for (Map.Entry innerEntry : ((Map<String, String>) outEntry.getValue()).entrySet()) {
                        this.info.println("About to make a persist switch take effect");
                        String beanName = (String) outEntry.getKey();
                        String fieldName = (String) innerEntry.getKey();
                        String newValueStr = (String) innerEntry.getValue();
                        if (this.switches.get(beanName) != null) {
                            if (((Map) this.switches.get(beanName)).get(fieldName) != null) {
                                SwitchInfo si = (SwitchInfo) ((Map) this.switches.get(beanName)).get(fieldName);
                                try {
                                    setCurrentSwitchValueOfABean(beanName, newValueStr, si);
                                } catch (Exception e) {
                                    this.info.println("Taking switch effect exception, beanName:" + beanName + ",fieldName:" + fieldName
                                            + ",newValue" + newValueStr);
                                    this.info.println(ManagerLoggerUtil.convertThrowableToString(e));
                                }
                            } else {
                                this.info.println("Invalid fieldName, beanName:" + beanName + ",fieldName:" + fieldName + ",newValue" + newValueStr);
                            }
                        } else {
                            this.info.println("Invalid beanName, beanName:" + beanName + ",fieldName:" + fieldName + ",newValue" + newValueStr);
                        }
                    }
                }
            }
        }
        Map.Entry outEntry;
    }

    @SuppressWarnings({ "resource", "unchecked", "rawtypes" })
    private Map<String, Map<String, String>> populateWithPersistFile(File persistFile) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(persistFile));
        String line = null;
        Map<String, Map<String, String>> persistInfo = new HashMap<String, Map<String, String>>();
        while ((line = in.readLine()) != null) {
            int beanNameIndex = line.indexOf("BeanName");
            int fieldNameIndex = line.indexOf("FieldName");
            int switchValueIndex = line.indexOf("SwitchValue");
            if ((beanNameIndex < 0) || (fieldNameIndex < 0) || (switchValueIndex < 0)) {
                throw new RuntimeException("The persist file's content is not valid, line:" + line);
            }

            String persistBeanName = line.substring(9, fieldNameIndex - 1);
            String persistFieldName = line.substring(fieldNameIndex + 10, switchValueIndex - 1);
            String persistSwitchValue = line.substring(switchValueIndex + 12);
            if (persistInfo.get(persistBeanName) == null) {
                persistInfo.put(persistBeanName, new HashMap<String, String>());
            }
            ((Map) persistInfo.get(persistBeanName)).put(persistFieldName, persistSwitchValue);
        }

        in.close();
        return persistInfo;
    }

    private static class SwitchInfo {
        private String sname;

        private String sdesc;

        private Field field;

        private Method getMethod;

        private Method setMethod;

        public Method getGetMethod() {
            return this.getMethod;
        }

        public void setGetMethod(Method getMethod) {
            this.getMethod = getMethod;
        }

        public Method getSetMethod() {
            return this.setMethod;
        }

        public void setSetMethod(Method setMethod) {
            this.setMethod = setMethod;
        }

        public Field getField() {
            return this.field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public String getSname() {
            return this.sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSdesc() {
            return this.sdesc;
        }

        public void setSdesc(String sdesc) {
            this.sdesc = sdesc;
        }
    }

}
