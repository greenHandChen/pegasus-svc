package com.pegasus.activiti.custom;

import com.pegasus.activiti.custom.behavior.CuxParallelMultiInstanceBehavior;
import com.pegasus.activiti.custom.behavior.CuxSequentialMultiInstanceBehavior;
import com.pegasus.activiti.custom.behavior.CuxUserTaskActivityBehavior;
import com.pegasus.common.utils.ApplicationContextUtil;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.springframework.stereotype.Component;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
@Component
public class CuxBehaviorFactory extends DefaultActivityBehaviorFactory {

    /**
     * @Author: enHui.Chen
     * @Description: 自定义SequentialMulti
     * @Data 2019/9/23
     */
    @Override
    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new CuxSequentialMultiInstanceBehavior(activity, innerActivityBehavior);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义ParallelMulti
     * @Data 2019/9/23
     */
    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        ParallelMultiInstanceBehavior parallelBehavior = new CuxParallelMultiInstanceBehavior(activity, innerActivityBehavior);
        // 动态注册bean
        ApplicationContextUtil.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(parallelBehavior);
        return parallelBehavior;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义UserTask的行为
     * @Data 2019/9/23
     */
    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        return new CuxUserTaskActivityBehavior(userTask);
    }
}
