# SecondKill
基于Springboot的秒杀系统
# 版本3
1. 配置前后端分离
> 配置跨域处理，跨域也会就收cookie
```java
/**
 * @program: secondkill
 * @author: alian
 * @description: 跨域配置
 * @create: 2022-04-12 17:19
 **/

package alian.secondkill.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description: 允许跨域携带cookie
 * @Param:
 * @return:
 * @Author: alian
 * @Date: 2022/4/12
 */
@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
        corsConfiguration.addAllowedOriginPattern("*");// 允许跨域将cookie返回给前端
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
        corsConfiguration.setAllowCredentials(true);//支持安全证书。跨域携带cookie需要配置这个
        corsConfiguration.setMaxAge(3600L);//预检请求的有效期，单位为秒。设置maxage，可以避免每次都发出预检请求
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
        return new CorsFilter(source);
    }
}


```
2. 前后设置跨域处理
```javascript
 $.ajaxSetup({
            xhrFields: {
                withCredentials: true
            }
        });//
```
3. 直接将html文件放入nginx服务器下面就能够访问


