/**
 * @program: secondkill
 * @author: alian
 * @description: TreadLocal记录User
 * @create: 2022-04-08 09:54
 **/

package alian.secondkill.config;

import alian.secondkill.entity.User;
/** 
* @Description: 每次API请求都把User存入ThreadLocal中，如果不存在就从用UserTocket获取user并放入ThreadLocal中去.
* @Param: 
* @return: 
* @Author: alian
* @Date: 2022/4/8
*/
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}