package alian.secondkill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @program: secondkill
 * @author: alian
 * @description: 枚举接口返回状态类
 * @create: 2022-04-05 11:14
 **/

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200,"success"),
    ERROR(500,"服务端异常"),

    LOGIN_EMPTY_ERROR(50001,"用户名或者密码为空"),
    LOGIN_MOBILE_ERROR(50002,"用户名不和法"),
    LOGIN_NOT_EXIST_ERROR(50003,"用户不存在"),
    LOGIN_PASSWD_ERROR(50004,"密码错误"),
    LOGIN_OUTDATE_ERROR(50005,"登录过期"),

    SECONDKILL_NOSTOCKS_ERROR(50010,"没有库存"),
    SECONDKILL_REPECT_ERROR(50011,"每件商品限抢购一件")
    ;



    private final Integer code;
    private final String message;
}
