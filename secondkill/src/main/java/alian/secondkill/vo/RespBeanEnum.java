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
    ERROR(500,"服务端日常")
    ;

    private final Integer code;
    private final String message;
}
