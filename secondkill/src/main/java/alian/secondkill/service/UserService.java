package alian.secondkill.service;

import alian.secondkill.entity.User;
import alian.secondkill.vo.LoginVo;
import alian.secondkill.vo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author alian
 * @since 2022-04-05
 */
public interface UserService extends IService<User> {

    RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo);

    public User getByUserTicket(String userTicket, HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException;
}
