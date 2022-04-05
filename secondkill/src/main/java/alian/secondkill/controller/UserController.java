package alian.secondkill.controller;


import alian.secondkill.service.UserService;
import alian.secondkill.vo.LoginVo;
import alian.secondkill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-05
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    /** 
    * @Description: 登录请求接口
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/5
    */
    @ResponseBody
    @RequestMapping("/doLogin")
    public RespBean dologin(LoginVo loginVo){
        log.info("login接口");
        return userService.login(loginVo);
    }
}

