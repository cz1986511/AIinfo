package com.danlu.persist.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.springframework.util.CollectionUtils;

public class CommonUtil
{

    private static Map<String, List<Method>> methodMap = new ConcurrentHashMap<String, List<Method>>();
    public static int DEFAULT_LIMIT = 200; // 默认显示数量

    // 获取汉字全拼
    public static String getPinYin(String src)
    {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try
        {
            for (int i = 0; i < t0; i++)
            {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                }
                else
                {
                    // 如果不是汉字字符，直接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        }
        catch (BadHanyuPinyinOutputFormatCombination e)
        {
            e.printStackTrace();
        }
        return t4;
    }

    // 按照拼音首字母排序
    public static List<String> sortByPinYin(List<String> list)
    {
        if (!CollectionUtils.isEmpty(list))
        {
            Comparator<Object> comparator = Collator.getInstance(java.util.Locale.CHINESE);
            Collections.sort(list, comparator);
        }
        return list;
    }

    public static Map<String, List<String>> pakeageBrands(List<String> brandList)
    {

        Map<String, List<String>> brands = new TreeMap<String, List<String>>(
            new Comparator<String>()
            {

                public int compare(String a, String b)
                {
                    return a.compareTo(b);
                }
            });

        if (CollectionUtils.isEmpty(brandList))
        {
            return brands;
        }
        Iterator<String> iterator = brandList.iterator();
        while (iterator.hasNext())
        {
            String brand = iterator.next();
            String pinYin = getPinYin(brand);
            String alpha = String.valueOf(pinYin.charAt(0)).toUpperCase();
            if (!brands.containsKey(alpha))
            {
                brands.put(alpha, new ArrayList<String>());
            }
            brands.get(alpha).add(brand);
        }
        return brands;
    }

    public static Object convert2Bean(Map<String, Object> map, Object obj)
    {
        try
        {
            if (obj == null || map == null || map.isEmpty())
            {
                return obj;
            }
            Class classzz = obj.getClass();
            String propertyName = "";
            String methodName = "";
            Object value = null;
            List<Method> methods = getAllSetMethod(classzz);
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
                            method.invoke(obj, value);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public static List<Method> getAllSetMethod(Class classzz)
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
                    if (methodName.startsWith("set"))
                    {
                        setMethods.add(method);
                    }
                }
                methodMap.put(key, setMethods);
            }
        }
        return setMethods;
    }

    public static String encode(String str, String method)
    {
        MessageDigest md = null;
        String dstr = null;
        try
        {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return dstr;
    }

    public static void main(String[] args)
    {
        String resultString = encode("123456", "MD5");
        System.out.println(resultString);
    }

}
