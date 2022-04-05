/**
 * @program: secondkill
 * @author: alian
 * @description: md5加密工具
 * @create: 2022-04-05 11:54
 **/

package alian.secondkill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
//  后端自定义的盐，可以与前端的不一致
    private static final String salt = "1a2b3c4d";
    /**
    * @Description: 明文直接加密
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
    /**
    * @Description: 加盐进行加密
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass
                +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }
    /**
    * @Description: 连续进行加密过程
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }
    public static void main(String[] args) {

        System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cd7f9
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"),
                "1a2b3c4d"));
        System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));
    }
}
