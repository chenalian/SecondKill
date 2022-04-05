/**
 * @program: secondkill
 * @author: alian
 * @description: 全局异常处理器
 * @create: 2022-04-05 21:33
 **/

package alian.secondkill.exception;

import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
     public RespBean ExceptionHandler(Exception e) {
            if (e instanceof GlobalException) {
                 GlobalException ex = (GlobalException) e;
                 return RespBean.error(ex.getRespBeanEnum());
         } else if (e instanceof BindException) {
                 BindException ex = (BindException) e;
                 RespBean respBean = RespBean.error(RespBeanEnum.ERROR);
                 respBean.setMessage("参数校验异常：" +
                    ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
                 return respBean;
         }return RespBean.error(RespBeanEnum.ERROR);
     }
}
