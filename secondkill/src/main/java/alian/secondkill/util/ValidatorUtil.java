/**
 * @program: secondkill
 * @author: alian
 * @description: 验证器工具
 * @create: 2022-04-05 19:23
 **/

package alian.secondkill.util;


import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
     // 手机号码正则表达式
     private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");
     /** 
     * @Description: 判断是否是电话号码
     * @Param: 
     * @return: 
     * @Author: alian
     * @Date: 2022/4/5
     */
     public static boolean isMobile(String mobile){
            if (StringUtils.isEmpty(mobile)) {
                 return false;
         }
            Matcher matcher = mobile_pattern.matcher(mobile);
            return matcher.matches();
     }
}
