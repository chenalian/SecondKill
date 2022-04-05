/**
 * @program: secondkill
 * @author: alian
 * @description: 登录参数
 * @create: 2022-04-05 19:45
 **/

package alian.secondkill.vo;


import alian.secondkill.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    @IsMobile
    @NotNull
    private String mobile;

    @NotNull
    private String password;
}
