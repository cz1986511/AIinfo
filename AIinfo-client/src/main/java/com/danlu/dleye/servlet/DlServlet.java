package com.danlu.dleye.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.danlu.dleye.constants.ManagerType;
import com.danlu.dleye.manager.DleyeManager;
import com.danlu.dleye.util.ManagerLoggerUtil;

public class DlServlet extends HttpServlet {

    /**
     * @author chenzhuo
     */
    private static final long serialVersionUID = 123132132143254L;

    private static Logger logger = LoggerFactory.getLogger(DlServlet.class);

    private Map<ManagerType, DleyeManager> managerMap = new HashMap<ManagerType, DleyeManager>();

    @SuppressWarnings("rawtypes")
    public void init() {
        PrintStream bootstrapLogger = ManagerLoggerUtil.getBootstrapLogger();
        try {
            bootstrapLogger.println("AgentServlet初始化休眠（2s）");
            TimeUnit.SECONDS.sleep(2L);
            bootstrapLogger.println("AgentServlet结束休眠");
        } catch (InterruptedException e1) {
            bootstrapLogger.println("AgentServlet初始化休眠被interrupt");
        }

        Map<String, Object> beans = new HashMap<String, Object>();

        beans = getAllBeans(getServletContext());

        bootstrapLogger.flush();
        ManagerLoggerUtil.invalidBootstrapStream();

        for (ManagerType type : ManagerType.values()) {
            DleyeManager manager = null;
            Class managerClass = type.getManagerClass();
            try {
                manager = (DleyeManager) managerClass.newInstance();
            } catch (Throwable e) {
                logger.error("实例化" + managerClass.getName() + "失败", e);
                continue;
            }
            try {
                PrintStream managerLogger = ManagerLoggerUtil.getManagerLogger(type);
                manager.init(getServletContext(), managerLogger, beans);
                managerLogger.flush();
                ManagerLoggerUtil.invalidInitLogger(type);
            } catch (Throwable e) {
                logger.error("初始化" + managerClass.getName() + "异常", e);
                continue;
            }
            this.managerMap.put(type, manager);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age","60");
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");
        ManagerType managerType = ManagerType.valueOf(type);
        if (managerType != null) {
            DleyeManager manager = (DleyeManager) this.managerMap.get(managerType);
            if (manager != null)
                out.println(manager.service(convertParamMap(request.getParameterMap())));
            else
                logger.error("未找到针对请求类型:" + type + "的Manager");
        } else {
            logger.error("无效的请求类型Type:" + type);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        doGet(request, response);
    }

    public static Map<String, Object> getAllBeans(ServletContext servletContext) {
        Map<String, Object> beans = new HashMap<String, Object>();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                Object bean = applicationContext.getBean(beanName);
                if (bean != null) {
                    beans.put(beanName, bean);
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        return beans;
    }

    @SuppressWarnings("rawtypes")
    private Map<String, String> convertParamMap(Map<String, String[]> oriQueryMap) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry entry : oriQueryMap.entrySet()) {
            if (((String[]) entry.getValue()).length > 0) {
                map.put((String) entry.getKey(), ((String[]) entry.getValue())[0]);
            }
        }
        return map;
    }

}
