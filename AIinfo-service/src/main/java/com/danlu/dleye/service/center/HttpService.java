package com.danlu.dleye.service.center;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpService
{

    private static Logger logger = LoggerFactory.getLogger(HttpService.class);
    private final static String GET_STRING = "GET";
    private final static String POST_STRING = "POST";

    /**
     * @Title httpsRequest
     * @Description https请求方法
     * @param requestUrl 请求地址 requestMethod 请求方法(GET/POST) outputStr
     * @return JSONObject
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr)
    {
        JSONObject jsonObject = null;
        try
        {
            X509TrustManager tm = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                                                                                   throws CertificateException
                {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                                                                                   throws CertificateException
                {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            if (null != outputStr)
            {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        }
        catch (ConnectException ce)
        {
            logger.error("连接超时：{}", ce);
        }
        catch (Exception e)
        {
            logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    /**
     * @ http请求方法
     * 
     * @param requestUrl: 请求地址 requestMethod: 请求方法 params:
     *        JSONObject.toJSONString(List<T> list)
     * @return JSONObject
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String params)
    {
        JSONObject jsonObject = null;
        if (!StringUtils.isBlank(requestUrl) && !StringUtils.isBlank(requestMethod))
        {
            try
            {
                HttpClient httpClient = new HttpClient();
                httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
                httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
                Header header = new Header();
                header.setName("X-ApiVersion");
                header.setValue("1.0");
                if (GET_STRING.equals(requestMethod))
                {
                    GetMethod gmethod = new GetMethod(requestUrl);
                    gmethod.addRequestHeader(header);
                    httpClient.executeMethod(gmethod);
                    if (200 == gmethod.getStatusCode())
                    {
                        return JSONObject.parseObject(gmethod.getResponseBodyAsString());
                    }
                    else
                    {
                        logger.error("httpRequest code:" + gmethod.getStatusCode() + " |url:"
                                     + requestUrl + " |requestMethod:" + requestMethod
                                     + " |params:" + params);
                    }
                    gmethod.releaseConnection();
                }
                else if (POST_STRING.equals(requestMethod))
                {
                    PostMethod pmethod = new PostMethod(requestUrl);
                    RequestEntity requestEntity = new StringRequestEntity(params,
                        "application/json", "utf-8");
                    pmethod.setRequestEntity(requestEntity);
                    pmethod.addRequestHeader(header);
                    httpClient.executeMethod(pmethod);
                    if (200 == pmethod.getStatusCode())
                    {
                        return JSONObject.parseObject(pmethod.getResponseBodyAsString());
                    }
                    else
                    {
                        logger.error("httpRequest code:" + pmethod.getStatusCode() + " |url:"
                                     + requestUrl + " |requestMethod:" + requestMethod
                                     + " |params:" + params);
                    }
                    pmethod.releaseConnection();
                }

            }
            catch (Exception e)
            {
                logger.error("httpRequest is Exception:" + e.toString());
            }
        }
        else
        {
            logger.error("httpRequest url or method is null");
        }
        return jsonObject;
    }

}
