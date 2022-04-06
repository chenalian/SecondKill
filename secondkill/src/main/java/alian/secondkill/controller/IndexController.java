package alian.secondkill.controller;


import alian.secondkill.entity.User;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/")
    public String index(){
        log.info("测试默认接口");
        return "login";
    }
}
