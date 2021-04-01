package xiaozhuo.info.web.common.config;

import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xiaozhuo.info.service.util.RedisClient;
import xiaozhuo.info.web.common.auth.RequireLoginAuth;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 权限过滤和拦截
 * @author chenzhuo
 * @date   2021-04-01
 */
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private static final String USER_KEY = "user_key";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequireLoginAuth requireLoginAuth = method.getAnnotation(RequireLoginAuth.class);
            if (requireLoginAuth != null) {
                //验证用户是否登录
                String userInfo = (String) RedisClient.get(USER_KEY,
                        new TypeReference<String>() {
                        });
                if (userInfo == null) {
                    throw new Exception("权限验证失败或登录过期，请重新登录");
                }
            }
            return true;
        }
        return true;
    }
}
