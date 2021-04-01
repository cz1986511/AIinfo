package xiaozhuo.info.web.common.auth;

import java.lang.annotation.*;

/**
 * 权限注解，需要登录
 * @author chenzhuo
 * @date   2021-04-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLoginAuth {
}
