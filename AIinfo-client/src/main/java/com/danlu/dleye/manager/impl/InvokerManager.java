package com.danlu.dleye.manager.impl;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.annotation.DleyeInvoker;
import com.danlu.dleye.constants.InvokerType;
import com.danlu.dleye.constants.ManagerType;
import com.danlu.dleye.manager.DleyeManager;
import com.danlu.dleye.util.DleyePrintStreamFactory;
import com.danlu.dleye.util.ManagerLoggerUtil;

public class InvokerManager implements DleyeManager {

    private static Logger logger = LoggerFactory.getLogger(InvokerManager.class);

    private PrintStream info;

    private Map<String, Object> beans;

    private static Map<Class<?>, Class<?>> typeConverter = new HashMap<Class<?>, Class<?>>();

    Map<String, Map<String, MethodInfo>> invokers;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public InvokerManager() {
        this.info = null;
        this.beans = new HashMap();

        typeConverter.put(Boolean.TYPE, Boolean.class);
        typeConverter.put(Boolean.class, Boolean.class);
        typeConverter.put(Byte.TYPE, Byte.class);
        typeConverter.put(Byte.class, Byte.class);
        typeConverter.put(Short.TYPE, Short.class);
        typeConverter.put(Short.class, Short.class);
        typeConverter.put(Character.TYPE, Character.class);
        typeConverter.put(Character.class, Character.class);
        typeConverter.put(Integer.TYPE, Integer.class);
        typeConverter.put(Integer.class, Integer.class);
        typeConverter.put(Long.TYPE, Long.class);
        typeConverter.put(Long.class, Long.class);
        typeConverter.put(Float.TYPE, Float.class);
        typeConverter.put(Float.class, Float.class);
        typeConverter.put(Double.TYPE, Double.class);
        typeConverter.put(Double.class, Double.class);

        typeConverter.put(String.class, String.class);
        typeConverter.put(Date.class, Date.class);

        this.invokers = new HashMap();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String service(Map<String, String> queryParams) {
        String action = (String) queryParams.get("action");
        if ("list".equalsIgnoreCase(action)) {
            JSONArray methods = queryAllInvokersInfo();
            return methods.toString();
        }
        if ("single".equalsIgnoreCase(action)) {
            String beanName = (String) queryParams.get("beanName");
            String signature = (String) queryParams.get("signature");
            JSONObject method = querySingleInvokerInfo(beanName, signature);
            return method.toString();
        }
        if ("invoke".equalsIgnoreCase(action)) {
            JSONArray array = new JSONArray();
            JSONObject json = new JSONObject();
            JSONObject method = null;
            String beanName = (String) queryParams.get("beanName");
            String signature = (String) queryParams.get("signature");
            if (this.invokers.get(beanName) != null) {
                if (((Map) this.invokers.get(beanName)).get(signature) != null) {
                    method = querySingleInvokerInfo(beanName, signature);
                    MethodInfo mi = (MethodInfo) ((Map) this.invokers.get(beanName)).get(signature);
                    Object[] paramValues = null;
                    if (mi.getParamCount() > 0) {
                        paramValues = new Object[mi.getParamCount()];
                        for (int i = 0; i < mi.getParams().size(); i++) {
                            ParamInfo pi = (ParamInfo) mi.getParams().get(i);
                            String paramValueStr = (String) queryParams.get("param" + (i + 1));
                            if (paramValueStr != null) {
                                Object paramValue = null;
                                try {
                                    paramValue = pi.convertFromString(paramValueStr);
                                    paramValues[i] = paramValue;
                                } catch (Exception e) {
                                    json.put("success", "false");
                                    json.put("errorMsg", "The " + (i + 1) + "th param is malformed: " + paramValueStr);
                                    array.add(json);
                                    array.add(method);
                                    return array.toString();
                                }
                            } else {
                                json.put("success", "false");
                                json.put("errorMsg", "The " + (i + 1) + "th param is missing or null");
                                array.add(json);
                                array.add(method);
                                return array.toString();
                            }
                        }
                    }
                    try {
                        DleyePrintStreamFactory.put();
                        Object bean = this.beans.get(beanName);
                        Object value = null;
                        if (((bean instanceof Advised)) && (mi.isTargetSpecial())) {
                            value = mi.getMethod().invoke(((Advised) bean).getTargetSource().getTarget(), paramValues);
                        } else {
                            value = mi.getMethod().invoke(this.beans.get(beanName), paramValues);
                        }
                        json.put("success", "true");
                        if (value != null) {
                            json.put("returnValue", convertOutputToHtmlForm(value.toString()));
                        } else {
                            json.put("returnValue", "void");
                        }
                        String dleye = DleyePrintStreamFactory.getDleyeOut();
                        json.put("dleye", dleye);
                    } catch (InvocationTargetException ie) {
                        json.put("success", "true");
                        json.put("exception", ManagerLoggerUtil.convertThrowableToString(ie.getCause() != null ? ie.getCause() : ie));
                    } catch (Exception e) {
                        json.put("success", "true");
                        json.put("exception", ManagerLoggerUtil.convertThrowableToString(e));
                    }
                } else {
                    json.put("success", "false");
                    json.put("errorMsg", "Invalid Signature:" + signature);
                }
            } else {
                json.put("success", "false");
                json.put("errorMsg", "Invalid BeanName:" + beanName);
            }
            array.add(json);
            array.add(method);
            return array.toString();
        }

        JSONObject json = new JSONObject();
        json.put("success", "false");
        json.put("errorMsg", "Invalid   type:" + action);
        return json.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private JSONArray queryAllInvokersInfo() {
        JSONArray methods = new JSONArray();
        String beanName;
        for (Map.Entry entry_out : this.invokers.entrySet()) {
            beanName = (String) entry_out.getKey();
            Map<String, MethodInfo> value = (Map<String, MethodInfo>) entry_out.getValue();
            for (Map.Entry entry_in : value.entrySet()) {
                String signature = (String) entry_in.getKey();
                JSONObject method = querySingleInvokerInfo(beanName, signature);
                methods.add(method);
            }
        }
        return methods;
    }

    @SuppressWarnings("rawtypes")
    private JSONObject querySingleInvokerInfo(String beanName, String signature) {
        JSONObject json = new JSONObject();
        if (this.invokers.get(beanName) != null) {
            if (((Map) this.invokers.get(beanName)).get(signature) != null) {
                MethodInfo mi = (MethodInfo) ((Map) this.invokers.get(beanName)).get(signature);
                json.put("beanName", beanName);
                json.put("signature", signature);
                json.put("desc", mi.getDesc());
                json.put("paramCount", Integer.valueOf(mi.getParamCount()));
                json.put("paramDesc", mi.getParamDesc());
                json.put("invokerType", mi.getType());
                JSONArray params = new JSONArray();
                for (ParamInfo pi : mi.getParams()) {
                    JSONObject param = new JSONObject();
                    param.put("type", pi.getParamType().getName());
                    params.add(param);
                }
                json.put("params", params);
            } else {
                json.put("success", "false");
                json.put("errorMsg", "Invalid Signature:" + signature);
            }
        } else {
            json.put("success", "false");
            json.put("errorMsg", "Invalid BeanName:" + beanName);
        }
        return json;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void init(ServletContext servletContext, PrintStream initLogger, Map<String, Object> beans) {
        this.info = initLogger;
        this.beans = beans;

        for (Map.Entry entry : beans.entrySet()) {
            String beanName = (String) entry.getKey();
            Object bean = entry.getValue();
            Map invokersOfABean = getAllInvokersOfABean(beanName, bean);
            this.invokers.put(beanName, invokersOfABean);
        }
        logger.info("MethodInvokers' size is : " + getMonitoredMethodCount());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<String, MethodInfo> getAllInvokersOfABean(String beanName, Object bean) {
        Map invokersOfABean = new HashMap();
        Class c = bean.getClass();
        Class originClass = AopUtils.getTargetClass(bean);
        this.info.println();
        this.info.println("=====Scanning Bean named: " + beanName + " start");
        this.info.println("Class: " + c.getCanonicalName() + " OriginClass: " + originClass.getCanonicalName());
        for (Method m : originClass.getMethods()) {
            DleyeInvoker iv = (DleyeInvoker) m.getAnnotation(DleyeInvoker.class);
            if (iv != null) {
                MethodInfo mi = null;

                if (c != originClass) {
                    try {
                        Method proxyMethod = c.getMethod(m.getName(), m.getParameterTypes());
                        mi = new MethodInfo(proxyMethod);
                    } catch (Exception e) {
                        this.info.println("Exception generated when checking the counter method named: " + m.getName() + " from class named: "
                                + c.getCanonicalName());
                        e.printStackTrace(this.info);

                        mi = new MethodInfo(m);

                        mi.setTargetSpecial(true);
                    }

                } else {
                    mi = new MethodInfo(m);
                }
                if ((iv.description() != null) && (!"".equals(iv.description()))) {
                    mi.setDesc(iv.description());
                }
                if ((iv.paraDesc() != null) && (!"".equals(iv.paraDesc()))) {
                    mi.setParamDesc(iv.paraDesc());
                }
                mi.setType(iv.type());
                invokersOfABean.put(mi.getSignature(), mi);
                this.info.println("Monitored Method: " + mi.getSignature());
            } else {
                this.info.println("Ordinary Method: " + m.getName());
            }
        }
        this.info.println("=====Scanning Bean named: " + beanName + " end");
        this.info.println();
        return invokersOfABean;
    }

    @Override
    public ManagerType getType() {
        return ManagerType.INVOKER;
    }

    private String convertOutputToHtmlForm(String originOuput) {
        if (originOuput != null) {
            return originOuput.replace("\n", "<br/>");
        }

        return "void";
    }

    @SuppressWarnings("rawtypes")
    private int getMonitoredMethodCount() {
        int count = 0;
        for (Map methodsOfABean : this.invokers.values()) {
            count += methodsOfABean.size();
        }
        return count;
    }

    private static class MethodInfo {
        private String paramDesc = "";

        private Method method;

        private String signature;

        private int paramCount;

        private String desc;

        private boolean targetSpecial = false;

        private InvokerType type;

        private List<InvokerManager.ParamInfo> params = new ArrayList<ParamInfo>();

        public MethodInfo(Method method) {
            this.method = method;
            this.paramCount = method.getParameterTypes().length;
            generateSignatureAndParamInfo();
        }

        @SuppressWarnings("rawtypes")
        private void generateSignatureAndParamInfo() {
            StringBuilder sign = new StringBuilder();
            sign.append(this.method.getName());
            sign.append("(");
            boolean endsWithComma = false;
            for (Class paramType : this.method.getParameterTypes()) {
                this.params.add(new InvokerManager.ParamInfo(paramType));
                sign.append(paramType.getName());
                sign.append(",");
                endsWithComma = true;
            }

            if (endsWithComma) {
                sign.deleteCharAt(sign.length() - 1);
            }
            sign.append(")");
            this.signature = sign.toString();
            this.desc = this.signature;
        }

        public InvokerType getType() {
            return this.type;
        }

        public void setType(InvokerType type) {
            this.type = type;
        }

        public String getSignature() {
            return this.signature;
        }

        public int getParamCount() {
            return this.paramCount;
        }

        public List<InvokerManager.ParamInfo> getParams() {
            return this.params;
        }

        public Method getMethod() {
            return this.method;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getParamDesc() {
            return this.paramDesc;
        }

        public void setParamDesc(String paramDesc) {
            this.paramDesc = paramDesc;
        }

        public boolean isTargetSpecial() {
            return this.targetSpecial;
        }

        public void setTargetSpecial(boolean targetSpecial) {
            this.targetSpecial = targetSpecial;
        }
    }

    private static class ParamInfo {
        private Class<?> paramType;

        public Class<?> getParamType() {
            return this.paramType;
        }

        public ParamInfo(Class<?> paramType) {
            this.paramType = paramType;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Object convertFromString(String paramValueStr) throws Exception {
            if ("null".equalsIgnoreCase(paramValueStr)) return null;
            Object paramValue = null;
            Class bigType = (Class) InvokerManager.typeConverter.get(this.paramType);
            if (bigType == String.class) {
                paramValue = paramValueStr;
            } else if (bigType == Character.class) {
                if (paramValueStr.length() > 0) {
                    paramValue = Character.valueOf(paramValueStr.toCharArray()[0]);
                } else {
                    paramValue = null;
                }
                paramValue = Character.valueOf(paramValueStr.toCharArray()[0]);
            } else if (bigType == Date.class) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(paramValueStr);
                paramValue = date;
            } else {
                Method transformedMethod = bigType.getMethod("valueOf", new Class[] { String.class });
                paramValue = transformedMethod.invoke(null, new Object[] { paramValueStr });
            }
            return paramValue;
        }
    }

}
