package alian.mybatisplusdemo;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class CodeGenerator {
    static final String URL = "jdbc:mysql://localhost:3306/secondkill?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8";

    public static void main(String[] args) {
        String projectPath = "D:/Java/SecondKill/MybatisPlusDemo";//获取项目路径,推荐手动更改，不用自动获取的
        FastAutoGenerator.create(URL, "root", "123456")
                //全局
                .globalConfig(builder -> {
                    builder.author("alian")
                            .outputDir(projectPath + "/src/main/java")//输出路径
                            .fileOverride()//覆盖文件
                            .disableOpenDir();//不打开文件夹
                })
                //包名配置
                .packageConfig(builder -> {
                    builder.parent("alian")
                            .moduleName("secondkill")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .entity("entity")
                            .mapper("mapper")
                            //自定义输出路径，mapper.xml生成到resources目录下
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper"));
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("t_user")
                            .addTablePrefix("t_")//表前缀
                            .serviceBuilder().formatServiceFileName("%sService")//去掉Service的 "I" 前缀
                            .controllerBuilder().enableRestStyle()//restful开启
                            .enableHyphenStyle()//url改变 例如：index_id_1
                            .entityBuilder().enableLombok();//开启lombok
                })
                //执行
                .execute();
    }
}