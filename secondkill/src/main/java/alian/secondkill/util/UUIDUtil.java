/**
 * @program: secondkill
 * @author: alian
 * @description: 用户标识
 * @create: 2022-04-06 08:59
 **/

package alian.secondkill.util;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid() {
         return UUID.randomUUID().toString().replace("-", "");
    }
}