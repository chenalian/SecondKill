/**
 * @program: secondkill
 * @author: alian
 * @description: 自定义全局异常
 * @create: 2022-04-05 21:31
 **/

package alian.secondkill.exception;

import alian.secondkill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    RespBeanEnum respBeanEnum;
}
