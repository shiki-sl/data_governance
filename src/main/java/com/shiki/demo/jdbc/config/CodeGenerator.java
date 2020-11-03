package com.shiki.demo.jdbc.config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiki
 * @description: TODO: 2020/6/17 快速链接
 * 代码生成器数据库配置 {@link CodeGenerator#getDataSourceConfig()}
 * 是否覆盖同名文件 {@link CodeGenerator#FLAG}
 * 修改作者{@link CodeGenerator#AUTHOR}
 * 选择数据库表{@link CodeGenerator#TEMPLATE_LIST}
 * 项目路径{@link CodeGenerator#PROJECT_PATH}
 * 输出文件夹{@link CodeGenerator#DIR_PATH}
 * 父包名{@link CodeGenerator#PARENT}
 * 模块名{@link CodeGenerator#MODULE_NAME}
 * @date 2020/5/28 18:01
 */
public class CodeGenerator {

    /**
     * 项目路径
     */
    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 输出到
     */
    private static final String DIR_PATH = "/../demo/src/main/java/";

    /**
     * 开发人员
     */
    private static final String AUTHOR = "shiki";

    /**
     * 父包名
     */
    private static final String PARENT = "com.shiki";

    /**
     * 模块名
     */
    private static final String MODULE_NAME = "auto";

    private static final boolean FLAG = false;

    private static String[] TEMPLATE_LIST = new String[]{
            "rm_user"
//            "user_live"
//            "live_log",
//            "branch",
//            "cast",
//            "column",
//            "column_video",
//            "hot_search",
//            "banner",
//            "live_cast",
//            "live_role",
//            "live_tag",
//            "log_comment",
//            "log_present",
//            "log_user_live",
//            "log_user_replay",
//            "log_user_video",
//            "present",
//            "replay",
//            "role",
//            "role_present"
//            "tag",
//            "user",
//            "user_collect",
//            "user_live_history",
//            "user_video_history",
//            "video"
//            "error_callback"
    };


    public static void main(String[] args) {
        if (ObjectUtils.isNotEmpty(args)) {
            TEMPLATE_LIST = args;
        }

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        mpg.setGlobalConfig(getGlobalConfig());
        // 数据源配置
        mpg.setDataSource(getDataSourceConfig());
        // 包配置
        mpg.setPackageInfo(getPackageConfig());
        // 自定义配置
        mpg.setCfg(getInjectionConfig());
        // 配置模板
//		mpg.setTemplate(getTemplateConfig());
        // 策略配置
        mpg.setStrategy(getStrategyConfig());
//		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


    /**
     * 数据源配置
     *
     * @return
     */
    private static DataSourceConfig getDataSourceConfig() {

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/ccxi_crc_proj_new?characterEncoding=utf8");
//        mb4_unicode_ci
//        zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&
//        &allowPublicKeyRetrieval=true
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        return dsc;
    }

    /**
     * 自定义配置
     * 注入配置，通过该配置，可注入自定义参数等操作以实现个性化操作
     *
     * @return
     */
    private static InjectionConfig getInjectionConfig() {

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("author", this.getConfig().getGlobalConfig().getAuthor());
//				map.put("datetime", DateUtil.now());
//				map.put("package", this.getConfig().getPackageInfo().get("parent"));
//				map.put("moduleName", this.getConfig().getPackageInfo().get("moduleName"));
//
//				this.setMap(map);
            }
        };
        // 自定义返回配置 Map 对象
        // 该对象可以传递到模板引擎通过 cfg.xxx 引用
//		cfg.setMap();
        // 自定义输出文件
        // 配置 FileOutConfig 指定模板文件、输出文件达到自定义文件生成目的
        // 自定义输出配置

//		// 如果模板引擎是 freemarker
//		String templatePath = "/templates/mapper.xml.ftl";
//		 如果模板引擎是 velocity
//		String templatePath = "/templates/mapper.xml.vm";
        List<FileOutConfig> focList = new ArrayList<>();
//		// 自定义配置会被优先输出
//		focList.add(new FileOutConfig(templatePath) {
//			@Override
//			public String outputFile(TableInfo tableInfo) {
//				// 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//				return projectPath + "/gcyl-financial/srcmain/resources/mapper/"
//					+ tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//			}
//		});
        cfg.setFileOutConfigList(focList);
        // 自定义判断是否创建文件
        // 实现 IFileCreate 接口
        // 该配置用于判断某个类是否需要覆盖创建，当然你可以自己实现差异算法 merge 文件
//		cfg.setFileCreate(new IFileCreate() {
//			@Override
//			public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
//				// 判断自定义文件夹是否需要创建
//				checkDir("调用默认方法创建的目录");
//				return false;
//			}
//		});

        // 注入自定义 Map 对象(注意需要setMap放进去)
//		cfg.setMap();
        return cfg;
    }


    /**
     * 全局策略配置
     * IdType.AUTO		数据库ID自增
     * IdType.INPUT		用户输入ID
     * IdType.ID_WORKER	全局唯一ID，内容为空自动填充（默认配置）
     * IdType.UUID		全局唯一ID，内容为空自动填充
     *
     * @return
     */
    private static GlobalConfig getGlobalConfig() {

        GlobalConfig gc = new GlobalConfig();
        // 生成文件的输出目录；默认值：项目目录
        gc.setOutputDir(PROJECT_PATH + DIR_PATH);
        // 是否覆盖同名文件，默认false
        gc.setFileOverride(FLAG);
        // 是否打开输出目录:true
        gc.setOpen(false);
        // 是否在xml中添加二级缓存配置
        gc.setEnableCache(false);
        // 开发人员
        gc.setAuthor(AUTHOR);
        // 开启 Kotlin 模式
        gc.setKotlin(false);
        // 开启 swagger2 模式
        gc.setSwagger2(true);
        // 开启 ActiveRecord 模式
        gc.setActiveRecord(true);
        // 开启 BaseResultMap
        gc.setBaseResultMap(true);
        // 开启 baseColumnList
        gc.setBaseColumnList(false);
        // 时间类型对应策略
//		gc.setDateType(DateType.TIME_PACK);
        // 实体命名方式 默认值：null 例如：%sEntity 生成 UserEntity
//		gc.setEntityName("");
        // mapper 命名方式
//		gc.setMapperName("");
        // Mapper xml 命名方式
//		gc.setXmlName("");
        // service 命名方式
//		gc.setServiceName("");
        // service impl 命名方式
//		gc.setServiceImplName("");
        // controller 命名方式
//		gc.setControllerName("");
        // 指定生成的主键的ID类型
//		gc.setIdType(IdType.AUTO);
        return gc;
    }

    /**
     * 模板配置
     * 模板配置，可自定义代码生成的模板，实现个性化操作
     *
     * @return
     */
    private static TemplateConfig getTemplateConfig() {

        TemplateConfig templateConfig = new TemplateConfig();
        // Java 实体类模板
        templateConfig.setEntity("/template/Entity.xml.vm");
        // Kotin 实体类模板
//		templateConfig.setEntityKt("/template/Mapper.xml.vm");
        // Service 类模板
        templateConfig.setService("/template/Service.xml.vm");
        // Service impl 实现类模板
        templateConfig.setServiceImpl("/template/ServiceImpl.xml.vm");
        // mapper 模板
        templateConfig.setMapper("/template/Mapper.java.vm");
        // mapper xml 模板
        templateConfig.setXml("/template/Mapper.xml.vm");
        // controller 控制器模板
        templateConfig.setController("/template/Controller.xml.vm");

        return templateConfig;
    }

    /**
     * 包名配置
     * 包名配置，通过该配置，指定生成代码的包路径
     *
     * @return
     */
    private static PackageConfig getPackageConfig() {

        PackageConfig pc = new PackageConfig();
        // 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        pc.setParent(PARENT);
        //父包模块名
        pc.setModuleName(MODULE_NAME);
        // Entity包名
//		pc.setEntity("");
        // Service包名
//		pc.setService("");
        // Service Impl包名
//		pc.setServiceImpl("");
        // Mapper包名
//		pc.setMapper("");
        // Mapper XML包名
//		pc.setXml("");
        // Controller包名
//		pc.setController("");
        // 路径配置信息
//		pc.setPathInfo();

        return pc;
    }

    /**
     * 表配置
     * 数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
     *
     * @return
     */
    private static StrategyConfig getStrategyConfig() {

        StrategyConfig strategy = new StrategyConfig();
        // 是否大写命名
        strategy.setCapitalMode(true);
        // 是否跳过视图
        strategy.setSkipView(false);
        // 驼峰=underline_to_camel；不变=no_change
        // 数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表前缀
//		strategy.setTablePrefix("tyg_");
        // 字段前缀
//		strategy.setFieldPrefix("");
        // 自定义继承的Entity类全称，带包名
//		strategy.setSuperEntityClass("");
        // 自定义基础的Entity类，公共字段
//		strategy.setSuperEntityColumns("");
        // 自定义继承的Mapper类全称，带包名
//		strategy.setSuperMapperClass("");
        // 自定义继承的Service类全称，带包名
//		strategy.setSuperServiceClass("");
        // 自定义继承的ServiceImpl类全称，带包名
//		strategy.setSuperServiceImplClass("");
        // 自定义继承的Controller类全称，带包名
//		strategy.setSuperControllerClass("");
        // 需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategy.setInclude(TEMPLATE_LIST);
        // 需要排除的表名，允许正则表达式
//		strategy.setExclude("");
        // 【实体】是否生成字段常量（默认 false）
        strategy.setEntityColumnConstant(false);
        // 【实体】是否为构建者模型（默认 false）
//        strategy.setChainModel(false);
        // 【实体】是否为lombok模型（默认 false）
        strategy.setEntityLombokModel(true);
        // Boolean类型字段是否移除is前缀（默认 false）
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        // 生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        // 驼峰转字符
        strategy.setControllerMappingHyphenStyle(false);
        //是否生成实体时，生成字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 乐观锁属性名称
        strategy.setVersionFieldName("version");
        // 逻辑删除属性名称
        strategy.setLogicDeleteFieldName("is_del");
        // 表填充字段
//		strategy.setTableFillList();
        return strategy;
    }
}
