package com.pegasus.activiti.config;

import com.pegasus.activiti.custom.CuxBehaviorFactory;
import com.pegasus.activiti.custom.CuxProcessEngineConfiguration;
import com.pegasus.activiti.custom.parser.CuxUserTaskParseHandler;
import com.pegasus.activiti.listener.MessageNoticeListener;
import com.pegasus.activiti.service.impl.ActivitiEmpServiceImpl;
import org.activiti.engine.*;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.rest.common.application.DefaultContentTypeResolver;
import org.activiti.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enHui.Chen on 2019/8/8.
 */
@Configuration
public class ActivitiConfiguration {

    @Autowired
    private CuxBehaviorFactory cuxBehaviorFactory;

    @Bean
    public RestResponseFactory restResponseFactory() {
        return new RestResponseFactory();
    }

    @Bean
    public DefaultContentTypeResolver defaultContentTypeResolver() {
        return new DefaultContentTypeResolver();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建流程引擎配置
     * @Data 2019/8/8
     */
    @Bean
    public CuxProcessEngineConfiguration processEngineConfiguration(PlatformTransactionManager dataSourceTransactionManager, DataSource dataSource) throws IOException {
        CuxProcessEngineConfiguration configuration = new CuxProcessEngineConfiguration();
        // 设置数据源/事务
        configuration.setTransactionManager(dataSourceTransactionManager);
        configuration.setDataSource(dataSource);
        // 自定义流程文件路径
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "processes/*.bpmn");
        configuration.setDeploymentResources(resources);
        // 设置数据库更新方式
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setDbIdentityUsed(false);
        // 设置自定义行为工厂
        configuration.setActivityBehaviorFactory(cuxBehaviorFactory);
        // 设置bpmn任务解析器
        List<BpmnParseHandler> bpmnParseHandlers = new ArrayList<>();
        bpmnParseHandlers.add(new CuxUserTaskParseHandler());
        configuration.setCustomDefaultBpmnParseHandlers(bpmnParseHandlers);
        // 设置activiti流程对象
        Map<Object, Object> beans = new HashMap<>();
        beans.put("activitiEmp", new ActivitiEmpServiceImpl());
        beans.put("messageNotice", new MessageNoticeListener());
        configuration.setBeans(beans);
        return configuration;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建流程引擎
     * @Data 2019/9/23
     */
    @Bean
    public ProcessEngine processEngine(@Autowired CuxProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 存储服务
     * @Data 2019/8/8
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 运行服务
     * @Data 2019/8/8
     */
    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 任务服务
     * @Data 2019/8/8
     */
    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 历史服务
     * @Data 2019/8/8
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }
}
