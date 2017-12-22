package com.danlu.dleye.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * 
 * @ClassName: DlBeanUtils
 * @Description: 丹露对象操作工具类
 * @author tangzhengpan
 * @date 2017年7月6日
 *
 */
public class DlBeanUtils extends BeanUtils
{
    private static final Logger logger = LoggerFactory.getLogger(DlBeanUtils.class);
    public static final String setMethodModify = "set";
    /**
     * 缓存各类set方法 key 类全路径，value当前类所有set方法集合
     */
    private static ConcurrentHashMap<String, List<Method>> methodMap = new ConcurrentHashMap<String, List<Method>>();

    /**
     * 拷贝复合对象集合到另一个复合对象集合（适用于两个复合对象集合中对象属性属性名称一致但属性数据类型不一致情况） 简单对象集合推荐适用{@link
     * com.danlu.dlcommon.utils.DlBeanUtils.copyListDTOProperties(List<? extends
     * Object>, Class<T>);
     */
    public static <T> List<T> copyListDTOProperties(List<? extends Object> dataList,
                                                    Class<T> classzz)
    {
        List<T> result = null;
        try
        {
            if (CollectionUtils.isNotEmpty(dataList) && classzz != null)
            {
                result = new ArrayList<T>();
                T t = null;
                for (Object obj : dataList)
                {
                    if (obj != null)
                    {
                        t = classzz.newInstance();
                        copyDTOProperties(obj, t);
                        result.add(t);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("拷贝集合复杂对象异常.", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 拷贝简单对象集合到另一个简单对象集合（适用于两个集合中对象属性数据类型完全一致）
     */
    public static <T> List<T> copyListProperties(List<? extends Object> dataList, Class<T> classzz)
    {
        List<T> result = null;
        try
        {
            if (CollectionUtils.isNotEmpty(dataList) && classzz != null)
            {
                result = new ArrayList<T>();
                T t = null;
                for (Object obj : dataList)
                {
                    if (obj != null)
                    {
                        t = classzz.newInstance();
                        copyProperties(obj, t);
                        result.add(t);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("拷贝集合简单对象异常.", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 拷贝对象数据复杂对象(适用于两个对象属性名称一致，属性数据类型不一致情况，简单对象推荐适用{@link
     * com.danlu.dlcommon.utils.DlBeanUtils.copyProperties(Object, Object)})
     */
    public static void copyDTOProperties(Object source, Object target)
    {
        if (source instanceof Collection)
        {
            throw new RuntimeException(
                "不支持集合复制对象拷贝.需要拷贝集合复制对象请参考com.danlu.dlcommon.utils.DlBeanUtils.copyListDTOProperties(List<? extends Object>, Class<T>)");
        }
        String json = JSONObject.toJSONString(source);
        ParserConfig config = new ParserConfig();
        config.setAsmEnable(false);
        org.springframework.beans.BeanUtils.copyProperties(
            JSONObject.parseObject(json, target.getClass(), config, new Feature[0]), target);
    }

    /**
     * 拷贝对象数据简单对象（适用于两个对象属性数据类型完全一致）
     */
    public static void copyProperties(Object source, Object target)
    {
        if (source instanceof Collection)
        {
            throw new RuntimeException(
                "不支持集合简单对象拷贝.需要拷贝集合简单对象请参考com.danlu.dlcommon.utils.DlBeanUtils.copyListProperties(List<? extends Object>, Class<T>)");
        }
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * 
     * @param bean 要转化的JavaBean 对象
     * @return 转化后 Map 对象
     */
    public static Map<String, Object> convertBeanToMap(Object bean)
    {
        try
        {
            Class<? extends Object> type = bean.getClass();
            Map<String, Object> returnMap = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor descriptor = null;
            String propertyName = null;
            Method readMethod = null;
            Object result = null;
            for (int i = 0; i < propertyDescriptors.length; i++)
            {
                descriptor = propertyDescriptors[i];
                propertyName = descriptor.getName();
                if (!propertyName.equals("class"))
                {
                    readMethod = descriptor.getReadMethod();
                    if (null != readMethod)
                    {
                        result = readMethod.invoke(bean, new Object[0]);
                        returnMap.put(propertyName, result);
                    }
                }
            }
            return returnMap;
        }
        catch (Exception e)
        {
            logger.error("转化bean=>map异常,bean=>{}", bean, e);
            throw new RuntimeException(e);
        }
    }

    /** 过滤掉集合中null元素及元素map中value为null或空字符串的key */
    public static void filterValIsBlank(List<Map<String, Object>> params)
    {
        if (CollectionUtils.isNotEmpty(params))
        {
            Iterator<Map<String, Object>> it = params.iterator();
            Map<String, Object> map = null;
            while (it.hasNext())
            {
                map = it.next();
                if (map == null)
                {
                    it.remove();
                    continue;
                }
                filterValIsBlank(map);
            }
        }
    }

    /** 过滤掉map中value为null或空字符串的key */
    public static void filterValIsBlank(Map<String, Object> param)
    {
        if (param != null && !param.isEmpty())
        {
            Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
            Map.Entry<String, Object> entry = null;
            while (it.hasNext())
            {
                entry = it.next();
                if (entry != null)
                {
                    if (entry.getValue() == null || "".equals(entry.getValue()))
                    {
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 获取class 所有set方法
     * 
     * @return: List<Method>
     */
    public static List<Method> getAllSetMethods(Class classzz)
    {
        List<Method> setMethods = new ArrayList<Method>();
        if (classzz != null)
        {
            String key = classzz.getName().trim();
            if (methodMap.containsKey(key))
            {
                return methodMap.get(key);
            }
            else
            {
                String methodName = "";
                Method[] methods = classzz.getMethods();
                for (Method method : methods)
                {
                    methodName = method.getName();
                    if (methodName.startsWith(DlBeanUtils.setMethodModify))
                    {
                        setMethods.add(method);
                    }
                }
                methodMap.put(key, setMethods);
            }
        }
        return setMethods;
    }

    /**
     * 转化map对象成指定t对象，map key=t属性名，value=属性值
     **
     * @param map 不能null且size > 0
     * @param classzz 不能为空或null
     */
    public static <T> T convertMapToBean(Map<String, Object> map, Class<T> classzz)
    {
        try
        {
            if (classzz == null || map == null || map.isEmpty())
            {
                throw new NullPointerException("class 为空或map为空.");
            }
            String propertyName = "";
            String methodName = "";
            Object value = null;
            T t = classzz.newInstance();
            List<Method> methods = getAllSetMethods(classzz);
            for (Method method : methods)
            {
                if (method != null)
                {
                    methodName = method.getName();
                    propertyName = methodName.substring(3, 4).toLowerCase(Locale.getDefault())
                                   + methodName.substring(4);
                    try
                    {
                        if (map.containsKey(propertyName))
                        {
                            value = map.get(propertyName);
                            method.invoke(t, value);
                        }
                    }
                    catch (Exception e)
                    {
                        throw e;
                    }
                }
            }
            return t;
        }
        catch (Exception e)
        {
            logger.error("转化map成bean异常,map=>{},class=>{}", map, classzz);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将dataList对象转化为classzz集合对象
     * 
     * @param dataList 不能null且size > 0
     * @param classzz 不能为空或null
     * @return List<T>
     */
    public static <T> List<T> convertToBeans(List<Map<String, Object>> dataList, Class<T> classzz)
    {
        try
        {
            List<T> rst = new ArrayList<T>();
            if (CollectionUtils.isNotEmpty(dataList))
            {
                T rst_obj = null;
                for (Map<String, Object> map : dataList)
                {
                    if (map != null && !map.isEmpty())
                    {

                        rst_obj = convertMapToBean(map, classzz);
                        if (rst_obj != null)
                        {
                            rst.add(rst_obj);
                            rst_obj = null;
                        }
                    }
                }
            }
            return rst;
        }
        catch (Exception e)
        {
            logger.error("批量转化List<Map<String,Object>>到对象集合异常.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将bean对象集合转为一个List<Map<String,Object>>
     * 
     * @param dataList 不能null且size > 0
     * @return List<Map<String,Object>>
     */
    public static <T> List<Map<String, Object>> convertBeansToMaps(List<? extends Object> dataList)
    {
        try
        {
            List<Map<String, Object>> rst = new ArrayList<Map<String, Object>>();
            if (CollectionUtils.isNotEmpty(dataList))
            {
                Map<String, Object> rst_obj = null;
                for (Object obj : dataList)
                {
                    if (obj != null)
                    {

                        rst_obj = convertBeanToMap(obj);
                        if (rst_obj != null)
                        {
                            rst.add(rst_obj);
                            rst_obj = null;
                        }
                    }
                }
            }
            return rst;
        }
        catch (Exception e)
        {
            logger.error("批量转换bean集合到List<Map<String,Object>>异常.", e);
            throw new RuntimeException(e);
        }
    }

    public static void bean2Map(Object sourceObj, Map<String, Object> targetMap)
    {
        Assert.notNull(sourceObj, "Source Object must not be null");
        Assert.notNull(targetMap, "Target Map must not be null");

        Class<?> sourceObjClazz = sourceObj.getClass();
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(sourceObjClazz);
        for (PropertyDescriptor sourcePd : sourcePds)
        {
            Method readMethod = sourcePd.getReadMethod();
            try
            {
                if (Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())
                    && !"class".equals(sourcePd.getName()))
                {
                    Object value = readMethod.invoke(sourceObj);
                    targetMap.put(sourcePd.getName(), value);
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not copy property '" + sourcePd.getName()
                                           + "' from sourceObj to targetMap", e);
            }
        }
    }

    public static void main(String[] args)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("1", "12");
        map.put("2", "");
        map.put("3", null);
        List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
        params.add(map);
        map = new HashMap<String, Object>();
        map.put("ad", "12");
        map.put("b", "");
        map.put("d", null);
        params.add(map);
        map = null;
        params.add(map);
        System.out.println(params);
        DlBeanUtils.filterValIsBlank(params);
        System.out.println(params);
    }
}
