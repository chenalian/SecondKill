package alian.secondkill.controller;


import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/")
    public String index(){
        log.info("测试默认接口");
        return "login";
    }
}
