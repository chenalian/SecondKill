package alian.secondkill.service;

import alian.secondkill.entity.User;
import alian.secondkill.vo.LoginVo;
import alian.secondkill.vo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author alian
 * @since 2022-04-05
 */
public interface UserService extends IService<User> {

    RespBean login(LoginVo loginVo);
}
