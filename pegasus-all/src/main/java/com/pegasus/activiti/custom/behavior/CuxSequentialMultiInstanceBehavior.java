package com.pegasus.activiti.custom.behavior;

import org.activiti.bpmn.model.Activity;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public class CuxSequentialMultiInstanceBehavior extends SequentialMultiInstanceBehavior {
    public CuxSequentialMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    protected int createInstances(DelegateExecution multiInstanceExecution) {
        return super.createInstances(multiInstanceExecution);
    }
}
