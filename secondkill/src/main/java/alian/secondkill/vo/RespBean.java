/**
 * @program: secondkill
 * @author: alian
 * @description: 接口统一返回数据
 * @create: 2022-04-05 11:14
 **/

package alian.secondkill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
    * @Description: 成功状态返回
    * @Param: null
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    public static  RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }
    /**
     * @Description: 成功状态返回
     * @Param: Object
     * @return:
     * @Author: alian
     * @Date: 2022/4/5
     */
    public static  RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), obj);
    }
    /**
    * @Description: 失败状态返回
    * @Param: RespBeanEnum失败对象
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }
}
