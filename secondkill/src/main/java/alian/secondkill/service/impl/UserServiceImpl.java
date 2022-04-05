package alian.secondkill.service.impl;

import alian.secondkill.entity.User;
import alian.secondkill.exception.GlobalException;
import alian.secondkill.mapper.UserMapper;
import alian.secondkill.service.UserService;
import alian.secondkill.util.MD5Util;
import alian.secondkill.util.ValidatorUtil;
import alian.secondkill.vo.LoginVo;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author alian
 * @since 2022-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public RespBean login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        /*
        *
        * 参数合法性判断，已经由自定义注解完成验证
        *
        * */
        //        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
        //            return RespBean.error(RespBeanEnum.LOGIN_EMPTY_ERROR);
        //        }
        //        if(!ValidatorUtil.isMobile(mobile)){
        //            return RespBean.error(RespBeanEnum.LOGIN_MOBILE_ERROR);
        //        }
        User user = userMapper.selectById(mobile);
        /*
        *
        * 统一异常处理
        *
        * */
        if(user==null){
//            return RespBean.error(RespBeanEnum.LOGIN_NOT_EXIST_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_NOT_EXIST_ERROR);
        }
        // 校验密码
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//             return RespBean.error(RespBeanEnum.LOGIN_PASSWD_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_PASSWD_ERROR);
        }
        return RespBean.success();
    }
}
