import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                .setAuthor("简单随风 - 自动生成代码")
                .setOpen(false)
                .setFileOverride(false)
                .setIdType(IdType.AUTO)
                .setBaseResultMap(true)
                .setEntityName("%sDO")
                .setServiceName("%sService");
        mpg.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl("jdbc:mysql://yourdatabase:3306/lyntest?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("username")
                .setPassword("password");
        mpg.setDataSource(dataSourceConfig);

        // 包名配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setParent("com.lyn")
                .setPathInfo(getPathInfo())
                .setEntity("model")
                .setController("controller")
                .setXml("xml");
        mpg.setPackageInfo(packageConfig);

        // 模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
                .setEntity("/mpg/templates/entity.java")
                .setXml("/mpg/templates/mapper.xml")
                .setController("/mpg/templates/controller.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setNaming(NamingStrategy.underline_to_camel)
                .setSuperEntityClass("com.lyn.model.BaseModel")
                .setTablePrefix("lin_")
                .setEntitySerialVersionUID(false)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
//                .setSuperEntityColumns("id", "create_time", "update_time", "delete_time")
                .setInclude(scanner("表名，多个英文逗号分割").split(","))
                .setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategyConfig);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 读取控制台内容
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    private static Map<String, String> getPathInfo() {
        Map<String, String> pathInfo = new HashMap<>();
        pathInfo.put(ConstVal.ENTITY_PATH, System.getProperty("user.dir") + "/src/main/java/com/lyn/model/track");
        pathInfo.put(ConstVal.MAPPER_PATH, System.getProperty("user.dir") + "/src/main/java/com/lyn/mapper/track");
        pathInfo.put(ConstVal.SERVICE_PATH, System.getProperty("user.dir") + "/src/main/java/com/lyn/service/track");
        pathInfo.put(ConstVal.SERVICE_IMPL_PATH, System.getProperty("user.dir") + "/src/main/java/com/lyn/service/track/impl");
        pathInfo.put(ConstVal.CONTROLLER_PATH, System.getProperty("user.dir") + "/src/main/java/com/lyn/controller/track");
        pathInfo.put(ConstVal.XML_PATH, System.getProperty("user.dir") + "/src/main/resources/mapper");
        return pathInfo;
    }
}
