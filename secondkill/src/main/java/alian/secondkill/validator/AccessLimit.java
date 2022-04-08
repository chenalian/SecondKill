package alian.secondkill.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @Description: 自定义注解-用于限制规定时间内访问的次数
* @Param:
* @return:
* @Author: alian
* @Date: 2022/4/8
*/
@Retention(RetentionPolicy.RUNTIME)//作用域运行时
@Target(ElementType.METHOD)//作用域方法上面
public @interface AccessLimit {
    // 时间
    int second();
    // 最大次数
    int maxCount();
    //是否需要登录
    boolean needLogin() default true;
}
